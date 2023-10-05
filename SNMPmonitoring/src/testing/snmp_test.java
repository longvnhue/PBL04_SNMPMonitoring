package testing;

import java.io.IOException;
import java.net.SocketException;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class snmp_test {
	public static void main(String[] args) {
		// String Agent = "udp:172.20.10.5/161";
		// String Agent2_virtual = "udp:192.168.159.129/161";
		// String community = "public";
		// String systemNameOID = ".1.3.6.1.2.1.1.5.0";
		// TransportMapping<?> transport;
		// try {
		// 	transport = new DefaultUdpTransportMapping();
		// 	transport.listen();
		// 	Snmp snmp = new Snmp(transport);
		// 	PDU pdu = new PDU();
		// 	pdu.setType(PDU.GET);
		// 	pdu.add(new VariableBinding(new OID(systemNameOID)));
		// 	CommunityTarget target = new CommunityTarget();
		// 	CommunityTarget target2 = new CommunityTarget();
			
		// 	target.setCommunity(new OctetString(community));
        //     target.setAddress(GenericAddress.parse(Agent));
        //     target.setVersion(SnmpConstants.version2c);

		// 	target2.setCommunity(new OctetString(community));
        //     target2.setAddress(GenericAddress.parse(Agent2_virtual));
        //     target2.setVersion(SnmpConstants.version2c);
            
        //     ResponseEvent response = snmp.get(pdu, target);
		// 	ResponseEvent response2 = snmp.get(pdu, target2);
        //     if (response != null && response.getResponse() != null) {
        //         VariableBinding vb = response.getResponse().get(0);
		// 		// VariableBinding vb2 = response2.getResponse().get(0);
		// 		// + "\nSys02 @name: " + vb2.getVariable().toString()
        //         System.out.println("Sys01 @name " + vb.getVariable().toString() );
        //     }
        //     else {
        //         System.err.println("No response");
        //     }
        //     snmp.close();
            
			
		// } catch (Exception e) {
		// 	// TODO Auto-generated catch block
		// 	e.printStackTrace();
		// }
		System.out.println("ABCV");
		
	}
}
