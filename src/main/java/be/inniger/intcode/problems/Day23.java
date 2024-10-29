package be.inniger.intcode.problems;

import be.inniger.intcode.IntCode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day23 {

    public static long partOne(List<Long> program) {
        return Network.of(50, program, true).runUntilAddressOutOfBounds().y;
    }

    public static long partTwo(List<Long> program) {
        return Network.of(50, program, false).runUntilAddressOutOfBounds().y;
    }

    private record Packet(long address, long x, long y) {
    }

    private static class NIC {

        private final long address;
        private final IntCode intCode;
        private final Queue<Packet> packetQueue;

        private IntCode.Status status;

        private NIC(long address, IntCode intCode, Queue<Packet> packetQueue, IntCode.Status status) {
            this.address = address;
            this.intCode = intCode;
            this.packetQueue = packetQueue;
            this.status = status;
        }

        public static NIC of(long address, List<Long> program) {
            var intCode = new IntCode(program);
            intCode.input().add(address);
            var status = intCode.run();

            return new NIC(address, intCode, new ArrayDeque<>(), status);
        }

        public void run() {
            if (status != IntCode.Status.INPUT_BLOCKED) {
                throw new IllegalStateException("IntCode with address " + address + " is not input blocked");
            }

            if (packetQueue.isEmpty()) {
                intCode.input().add(-1L);
            } else {
                var packet = packetQueue.remove();
                intCode.input().add(packet.x);
                intCode.input().add(packet.y);
            }

            status = intCode.run();
        }

        public long getAddress() {
            return address;
        }

        public void receive(Packet packet) {
            if (packet.address != address) {
                throw new IllegalStateException("NIC received the wrong packet: " + packet);
            }

            packetQueue.add(packet);
        }

        public Optional<Packet> send() {
            if (intCode.output().isEmpty()) {
                return Optional.empty();
            }

            var address = intCode.output().remove();
            var x = intCode.output().remove();
            var y = intCode.output().remove();

            return Optional.of(new Packet(address, x, y));
        }

        public boolean isIdle() {
            return packetQueue.isEmpty();
        }
    }

    private static class NAT {

        private long x = -1;
        private long y = -1;

        public void receive(Packet packet) {
            if (packet.address != 255) {
                throw new IllegalStateException("NAT received the wrong packet: " + packet);
            }

            this.x = packet.x;
            this.y = packet.y;
        }

        public Packet send() {
            return new Packet(0, x, y);
        }
    }

    private record Network(
            Map<Long, NIC> nics,
            Queue<Packet> packetQueue,
            NAT nat,
            boolean fastReturn,
            List<Long> natDelivered
    ) {

        public static Network of(long size, List<Long> program, boolean fastReturn) {
            var nics = LongStream.range(0, size)
                    .mapToObj(address -> NIC.of(address, program))
                    .collect(Collectors.toMap(NIC::getAddress, Function.identity()));
            return new Network(nics, new ArrayDeque<>(), new NAT(), fastReturn, new ArrayList<>());
        }

        public Packet runUntilAddressOutOfBounds() {
            while (true) {
                // Gather sent packets
                for (var nic : nics.values()) {
                    for (var packet = nic.send(); packet.isPresent(); packet = nic.send()) {
                        packetQueue.add(packet.get());
                    }
                }

                // Check if the network is idle
                var idle = packetQueue.isEmpty() && nics.values().stream().allMatch(NIC::isIdle);
                if (idle) {
                    var packet = nat.send();

                    if (natDelivered.contains(packet.y)) {
                        return packet;
                    }

                    natDelivered.add(packet.y);
                    packetQueue.add(packet);
                }

                // Route packets to recipients
                while (!packetQueue.isEmpty()) {
                    var packet = packetQueue.remove();

                    if (packet.address == 255 && fastReturn) {
                        return packet;
                    }
                    if (packet.address == 255) {
                        nat.receive(packet);
                    } else if (!nics.containsKey(packet.address)) {
                        throw new IllegalStateException("Cannot send packet to address " + packet.address);
                    } else {
                        var nic = nics.get(packet.address);
                        nic.receive(packet);
                    }
                }

                // Run all the NICs for the next iteration
                nics.values().forEach(NIC::run);
            }
        }
    }
}
