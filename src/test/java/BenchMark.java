import benchmark.*;
import com.politrons.grpc.benchmark.reactive.ReactiveBenchmarkClient;
import com.politrons.grpc.benchmark.reactive.ReactiveBenchmarkServer;
import com.politrons.grpc.benchmark.regular.RpcBenchMarkClient;
import com.politrons.grpc.benchmark.regular.RpcBenchMarkServer;
import finagle.asyncStream.StreamingClient;
import finagle.asyncStream.StreamingServer;
import finagle.thrift.rpc.ThriftRPCClient;
import finagle.thrift.rpc.ThriftRPCServer;
import org.junit.Test;

import java.io.IOException;
import java.util.stream.IntStream;

public class BenchMark {


    @Test
    public void RpcVsRest() {
        IntStream.range(0, 10).forEach(index -> {
            try {
                int port = 4000 + index * 3 + 2;
                HttpGrizzlyServer.start(port);
                long start = System.currentTimeMillis();
                HttpClientToGrizzly.run(port);
                System.out.println("Rest Grizzly http 1.0 - response time:" + ((System.currentTimeMillis() - start)) + " millis");

                int port1 = 5001 + index * 3 + 2;
                HttpFinagleServers.start(port1);
                start = System.currentTimeMillis();
                HttpClientToFinagle.run(port1);
                System.out.println("Rest Finagle http 1.0 - response time:" + ((System.currentTimeMillis() - start)) + " millis");

                int port2 = 6002 + index * 3 + 2;
                RpcBenchMarkServer.start(port2);
                start = System.currentTimeMillis();
                RpcBenchMarkClient.run(port2);
                System.out.println("gRPC regular - response time:" + ((System.currentTimeMillis() - start)) + " millis");

                int port3 = 7003 + index * 3 + 2;
                ReactiveBenchmarkServer.start(port3);
                start = System.currentTimeMillis();
                ReactiveBenchmarkClient.run(port3);
                System.out.println("gRPC Reactive response - time:" + ((System.currentTimeMillis() - start)) + " millis");

                int port4 = 8004 + index * 3 + 2;
                ThriftRPCServer.start(port4);
                start = System.currentTimeMillis();
                ThriftRPCClient.run(port4);
                System.out.println("Thrift RPC - response time:" + ((System.currentTimeMillis() - start)) + " millis");

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

}
