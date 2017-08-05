# Just a simple JMS-Remote-JNDI client for JBOSS

Because way too many times I had to look it up.

Maven should find all dependencies - Done with Java8 + Maven 3


## JBoss EAP 6 excerpt

```
...
        <subsystem xmlns="urn:jboss:domain:messaging:1.4">
            <hornetq-server>
...
                <connectors>
					<netty-connector name="netty" socket-binding="messaging"/>
                    <netty-connector name="netty-throughput" socket-binding="messaging-throughput">
                        <param key="batch-delay" value="50"/>
                    </netty-connector>
                    <in-vm-connector name="in-vm" server-id="0"/>
                </connectors>

                <acceptors>
                    <netty-acceptor name="netty" socket-binding="messaging"/>
                    <netty-acceptor name="netty-throughput" socket-binding="messaging-throughput">
                        <param key="batch-delay" value="50"/>
                        <param key="direct-deliver" value="false"/>
                    </netty-acceptor>
                    <in-vm-acceptor name="in-vm" server-id="0"/>
                </acceptors>
...
				<jms-connection-factories>
                    <connection-factory name="InVmConnectionFactory">
                        <connectors>
                            <connector-ref connector-name="in-vm"/>
                        </connectors>
                        <entries>
                            <entry name="java:/ConnectionFactory"/>
                        </entries>
                    </connection-factory>
                    <connection-factory name="RemoteConnectionFactory">
                        <connectors>
                            <connector-ref connector-name="netty"/>
                        </connectors>
                        <entries>
                            <entry name="java:jboss/exported/jms/RemoteConnectionFactory"/>
                        </entries>
                    </connection-factory>
...

    <socket-binding-group name="standard-sockets" default-interface="public" port-offset="${jboss.socket.binding.port-offset:0}">
... 
		<socket-binding name="messaging" port="5445"/>
        <socket-binding name="messaging-group" port="0" multicast-address="${jboss.messaging.group.address:231.7.7.7}" multicast-port="${jboss.messaging.group.port:9876}"/>
        <socket-binding name="messaging-throughput" port="5455"/>
        <socket-binding name="messaging-stomp" port="61613"/>
```


