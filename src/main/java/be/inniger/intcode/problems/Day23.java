package be.inniger.intcode.problems;

import be.inniger.intcode.IntCode;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day23 {

    public static long partOne(List<Long> program) {
        return Network.of(50, program).runUntilAddressOutOfBounds().y;
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

        public Optional<Packet> send() {
            if (intCode.output().isEmpty()) {
                return Optional.empty();
            }

            var address = intCode.output().remove();
            var x = intCode.output().remove();
            var y = intCode.output().remove();

            return Optional.of(new Packet(address, x, y));
        }

        public void receive(Packet packet) {
            packetQueue.add(packet);
        }
    }

    private record Network(Map<Long, NIC> nics, Queue<Packet> packetQueue) {

        public static Network of(long size, List<Long> program) {
            var nics = LongStream.range(0, size)
                    .mapToObj(address -> NIC.of(address, program))
                    .collect(Collectors.toMap(
                            NIC::getAddress,
                            Function.identity()
                    ));
            return new Network(nics, new ArrayDeque<>());
        }

        public Packet runUntilAddressOutOfBounds() {
            while (true) {
                // Gather sent packets
                for (var nic : nics.values()) {
                    for (var packet = nic.send(); packet.isPresent(); packet = nic.send()) {
                        packetQueue.add(packet.get());
                    }
                }

                // Route packets to recipients
                while (!packetQueue.isEmpty()) {
                    var packet = packetQueue.remove();

                    if (!nics.containsKey(packet.address)) {
                        return packet;
                    }

                    var nic = nics.get(packet.address);
                    nic.receive(packet);
                }

                // Run all the NICs for the next iteration
                nics.values().forEach(NIC::run);
            }
        }
    }
}
