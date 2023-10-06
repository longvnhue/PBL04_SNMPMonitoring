package testing;

import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import java.awt.TextField;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Label;
import java.io.IOException;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.OIDTextFormat;

import java.util.ArrayList;
import java.util.Base64;
import javax.swing.JToolBar;
import java.awt.Canvas;
import java.awt.Choice;


public class SNMP_interface extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private int dem = 0;
	public static int num_Logic_Processors = 0;

	public static final String SystName = ".1.3.6.1.2.1.1.5.0";
	public static final String SystUptime = ".1.3.6.1.2.1.1.3.0";
	public static final String SystContact = ".1.3.6.1.2.1.1.4.0";
	public static final String SystLocation = ".1.3.6.1.2.1.1.6.0";
	public static final String WifiTech = ".1.3.6.1.2.1.2.2.1.2.17";
	public static final String VPNTech = ".1.3.6.1.2.1.2.2.1.2.23";
	public static final String RiotPath = ".1.3.6.1.2.1.25.4.2.1.4.16036";
	public static final String IPv6LChange = ".1.3.6.1.2.1.55.1.5.1.11.2";
	public static final String PC = ".1.3.6.1.2.1.1.1.0";
	public static final String hrStorageSize = ".1.3.6.1.2.1.25.2.3.1.5.1";
	public static final String hrMemorySize = ".1.3.6.1.2.1.25.2.2.0";
	public static final String hrStorageUsed = ".1.3.6.1.2.1.25.2.3.1.6.1";
	public static final String hrProcessorLoad = ".1.3.6.1.2.1.25.3.3.1.2.7";
	public static final String hrSystemProcesses = ".1.3.6.1.2.1.25.1.6.0";
	public static final String Input_bandwidth = ".1.3.6.1.2.1.2.2.1.5.2";
	public static final String ipInReceies = ".1.3.6.1.4.1.2021.11.50";
	public static final String test = ".1.3.6.1.2.1.25.2.3.1.4.1";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SNMP_interface frame = new SNMP_interface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	//.Error FUnc
	public int getMAXDevice_Index(CommunityTarget target, Snmp snmp) {
		String oid = ".1.3.6.1.2.1.25.3.2.1.1.";
		int i = 1;
		Boolean j = true;
		ResponseEvent response = null;
		while (j == true) {
			i = i + 1;
			String newoid = oid + Integer.toString(i);
			PDU pdu = new PDU();
			pdu.add(new VariableBinding(new OID(newoid)));
			pdu.setType(PDU.GET);
			try {
				response = snmp.get(pdu, target);
				if (response == null || response.getResponse() == null) j = false;
				response = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return i;
	}
	
	public static int[] getFuLL_hrProcessorLoad(String agentIpAddress, String community) throws IOException{
		ArrayList<Integer> res = new ArrayList<Integer>();
		int cnt = 0;
		try {
		TransportMapping<?> transport = new DefaultUdpTransportMapping();
        transport.listen();
        Snmp snmp = new Snmp(transport);
        snmp.listen();
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(GenericAddress.parse(agentIpAddress));
        target.setVersion(SnmpConstants.version2c);
        target.setTimeout(3000);
        PDU pdu = new PDU();
        pdu.setType(PDU.GETNEXT);
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.1.25.3.3.1.2")));
        
        
        while (true) {
        	ResponseEvent event = snmp.getNext(pdu, target);
        	PDU response = event.getResponse();
        	 if (response == null || response.getErrorStatus() != PDU.noError) {
                 System.err.println("Request failed.");
                 break;
             }
        	 else {
        		 VariableBinding vb = response.get(0);
                 OID nextOID = vb.getOid();
                 if (nextOID.startsWith(new OID("1.3.6.1.2.1.25.3.3.1.2"))) {
                	 cnt++;
                	 res.add(Integer.parseInt(vb.getVariable().toString()));
                 }
                 else break;
                 pdu.setRequestID(new Integer32(0));
                 pdu.clear();
                 pdu.add(new VariableBinding(nextOID));
        	 }
        }
        snmp.close();
		}catch (Exception ex) {
            ex.printStackTrace();
        }
		int[] resBinding = new int[cnt];
		for (int i = 0 ; i < cnt ; i++) {
			resBinding[i] = res.get(i);
		}
		num_Logic_Processors = cnt;
		return resBinding;
	}
	
	
	public static int getMAXNum_hrStorageIndex(String agentIpAddress, String community)throws IOException{
		int result = -1;
		TransportMapping<?> transport = new DefaultUdpTransportMapping();
        transport.listen();
        Snmp snmp = new Snmp(transport);
        snmp.listen();
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(GenericAddress.parse(agentIpAddress));
        target.setVersion(SnmpConstants.version2c);
        target.setTimeout(3000);
        PDU pdu = new PDU();
        pdu.setType(PDU.GETNEXT);
        pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.25.2.3.1.1")));

        while (true) {
            ResponseEvent response = snmp.send(pdu, target);

            if (response != null && response.getResponse() != null) {
                VariableBinding vb = response.getResponse().get(0);
                OID hrStorageIndexOID = vb.getOid();
                
                if (hrStorageIndexOID.startsWith(new OID("1.3.6.1.2.1.25.2.3.1.1."))) {
                    int hrStorageIndexValue = hrStorageIndexOID.last();
    
                    // System.out.println("hrStorageIndex OID: " + hrStorageIndexOID);
                    // System.out.println("hrStorageIndex Value: " + hrStorageIndexValue);
                    result = Integer.max(result, hrStorageIndexValue);
                    pdu.clear();
                    pdu.setType(PDU.GETNEXT);
                    pdu.add(new VariableBinding(hrStorageIndexOID));
                } else {
                    // System.out.println("No more hrStorageIndex values found.");
                    break;
                }
            } else {
                // System.out.println("No more hrStorageIndex values found.");
                break;
            }
        }
        snmp.close();
        return result;
	}
	
	
	
	
	
	public static int getMAXNum_Interfaces(String agentIpAddress, String community) throws IOException {
        int interfaceCount = -1;
        
        try {
            TransportMapping<?> transport = new DefaultUdpTransportMapping();
            transport.listen();

            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString(community));
            target.setAddress(GenericAddress.parse(agentIpAddress));
            target.setVersion(SnmpConstants.version2c);
            target.setRetries(2);
            target.setTimeout(5000);

            Snmp snmp = new Snmp(transport);
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.2.1.0"))); // OID cho số lượng giao diện
            pdu.setType(PDU.GET);

            ResponseEvent event = snmp.send(pdu, target);
            if (event != null) {
                PDU responsePDU = event.getResponse();
                if (responsePDU != null) {
                    VariableBinding vb = responsePDU.get(0);
                    if (vb != null) {
                        interfaceCount = vb.getVariable().toInt();
                    }
                    else return 0;
                }
                else return 0;
            }
            else return 0;
            snmp.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return interfaceCount;
    }
	
	public static String hexToAscii(String s) {
		  int n = s.length();
		  StringBuilder sb = new StringBuilder(n / 2);
		  for (int i = 0; i < n; i += 2) {
		    char a = s.charAt(i);
		    char b = s.charAt(i + 1);
		    sb.append((char) ((hexToInt(a) << 4) | hexToInt(b)));
		  }
		  return sb.toString();
		}
	
	private static int hexToInt(char ch) {
		  if ('a' <= ch && ch <= 'f') { return ch - 'a' + 10; }
		  if ('A' <= ch && ch <= 'F') { return ch - 'A' + 10; }
		  if ('0' <= ch && ch <= '9') { return ch - '0'; }
		  throw new IllegalArgumentException(String.valueOf(ch));
		}
	
	public static String HextoCC(String hexString) {
		String[] hexArray = hexString.split(":");
		byte[] bytes = new byte[hexArray.length];

        for (int i = 0; i < hexArray.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hexArray[i], 16);
        }

        String result = new String(bytes);
		return result;
	}
	
	public static String[] GetFuLLName_Storage(String agentIpAddress, String community) throws IOException{
		int n = getMAXNum_hrStorageIndex(agentIpAddress, community);
		String[] res = new String[n];
		try {
            TransportMapping<?> transport = new DefaultUdpTransportMapping();
            transport.listen();

            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString(community));
            target.setAddress(GenericAddress.parse(agentIpAddress));
            target.setVersion(SnmpConstants.version2c);
            target.setRetries(2);
            target.setTimeout(5000);

            Snmp snmp = new Snmp(transport);
            PDU pdu = new PDU();
            String cdOID = ".1.3.6.1.2.1.25.2.3.1.3.";
            String OIDS[] = new String[n];
            for (int i = 1 ; i <= n ; i = i + 1) {
            	OIDS[i - 1] = cdOID + Integer.toString(i);
            }
            for (String oid : OIDS) {
                pdu.add(new VariableBinding(new OID(oid)));
            }
            
            pdu.setType(PDU.GET);

            ResponseEvent event = snmp.send(pdu, target);
            if (event != null) {
                PDU responsePDU = event.getResponse();
                if (responsePDU != null) {
                	for (int i = 0 ; i < n; i++) {
                		VariableBinding vb = event.getResponse().get(i);
                		res[i] = vb.getVariable().toString();		
                	}
                }
                else return null;
            }
            else return null;
            snmp.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return res;
	}
	
	
	
	
	public static int[] GetFuLL_hrStorageAllocationUnits(String agentIpAddress, String community) throws IOException{
		int n = getMAXNum_hrStorageIndex(agentIpAddress, community);
		int res[] = new int[n];
		try {
            TransportMapping<?> transport = new DefaultUdpTransportMapping();
            transport.listen();

            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString(community));
            target.setAddress(GenericAddress.parse(agentIpAddress));
            target.setVersion(SnmpConstants.version2c);
            target.setRetries(2);
            target.setTimeout(5000);

            Snmp snmp = new Snmp(transport);
            PDU pdu = new PDU();
            String cdOID = ".1.3.6.1.2.1.25.2.3.1.4.";
            String OIDS[] = new String[n];
            for (int i = 1 ; i <= n ; i = i + 1) {
            	OIDS[i - 1] = cdOID + Integer.toString(i);
            }
            for (String oid : OIDS) {
                pdu.add(new VariableBinding(new OID(oid)));
            }
            
            pdu.setType(PDU.GET);

            ResponseEvent event = snmp.send(pdu, target);
            if (event != null) {
                PDU responsePDU = event.getResponse();
                if (responsePDU != null) {
                	for (int i = 0 ; i < OIDS.length; i++) {
                		VariableBinding vb = event.getResponse().get(i);
                		String results = vb.getVariable().toString();
                		res[i] = Integer.parseInt(results);
                	}
                }
                else return null;
            }
            else return null;
            snmp.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return res;
	}
	
	public static double[] GetFuLL_hrStorageSize(String agentIpAddress, String community)throws IOException{
		int[] allocationUnits = GetFuLL_hrStorageAllocationUnits(agentIpAddress, community);
		int n = getMAXNum_hrStorageIndex(agentIpAddress, community);
		double[] res = new double[n];
		try {
            TransportMapping<?> transport = new DefaultUdpTransportMapping();
            transport.listen();

            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString(community));
            target.setAddress(GenericAddress.parse(agentIpAddress));
            target.setVersion(SnmpConstants.version2c);
            target.setRetries(2);
            target.setTimeout(5000);

            Snmp snmp = new Snmp(transport);
            PDU pdu = new PDU();
            String cdOID = ".1.3.6.1.2.1.25.2.3.1.5.";
            String OIDS[] = new String[n];
            for (int i = 1 ; i <= n ; i = i + 1) {
            	OIDS[i - 1] = cdOID + Integer.toString(i);
            }
            for (String oid : OIDS) {
                pdu.add(new VariableBinding(new OID(oid)));
            }
            
            pdu.setType(PDU.GET);

            ResponseEvent event = snmp.send(pdu, target);
            if (event != null) {
                PDU responsePDU = event.getResponse();
                if (responsePDU != null) {
                	for (int i = 0 ; i < OIDS.length; i++) {
                		VariableBinding vb = event.getResponse().get(i);
                		double results = Long.parseLong(vb.getVariable().toString()) * allocationUnits[i];
                		res[i] = (double)(results/1024/1024/1024);
                		
                	}
                }
                else return null;
            }
            else return null;
            snmp.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return res;
	}
	
	
	public static double[] GetFuLL_hrStorageUsed(String agentIpAddress, String community)throws IOException{
		int[] allocationUnits = GetFuLL_hrStorageAllocationUnits(agentIpAddress, community);
		int n = getMAXNum_hrStorageIndex(agentIpAddress, community);
		double[] res = new double[n];
		try {
            TransportMapping<?> transport = new DefaultUdpTransportMapping();
            transport.listen();

            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString(community));
            target.setAddress(GenericAddress.parse(agentIpAddress));
            target.setVersion(SnmpConstants.version2c);
            target.setRetries(2);
            target.setTimeout(5000);

            Snmp snmp = new Snmp(transport);
            PDU pdu = new PDU();
            String cdOID = ".1.3.6.1.2.1.25.2.3.1.6.";
            String OIDS[] = new String[n];
            for (int i = 1 ; i <= n ; i = i + 1) {
            	OIDS[i - 1] = cdOID + Integer.toString(i);
            }
            for (String oid : OIDS) {
                pdu.add(new VariableBinding(new OID(oid)));
            }
            
            pdu.setType(PDU.GET);

            ResponseEvent event = snmp.send(pdu, target);
            if (event != null) {
                PDU responsePDU = event.getResponse();
                if (responsePDU != null) {
                	for (int i = 0 ; i < OIDS.length; i++) {
                		VariableBinding vb = event.getResponse().get(i);
                		double results = Long.parseLong(vb.getVariable().toString()) * allocationUnits[i];
                		res[i] = (double)(results/1024/1024/1024);
                		
                	}
                }
                else return null;
            }
            else return null;
            snmp.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return res;
	}
	
	public static String[] GetFuLLName_Interface(String agentIpAddress, String community) throws IOException{
		
		int n = getMAXNum_Interfaces(agentIpAddress, community);
		String[] res = new String[n];
		try {
            TransportMapping<?> transport = new DefaultUdpTransportMapping();
            transport.listen();

            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString(community));
            target.setAddress(GenericAddress.parse(agentIpAddress));
            target.setVersion(SnmpConstants.version2c);
            target.setRetries(2);
            target.setTimeout(5000);

            Snmp snmp = new Snmp(transport);
            PDU pdu = new PDU();
            String cdOID = ".1.3.6.1.2.1.2.2.1.2.";
            String OIDS[] = new String[n];
            for (int i = 1 ; i <= n ; i = i + 1) {
            	OIDS[i - 1] = cdOID + Integer.toString(i);
            }
            for (String oid : OIDS) {
                pdu.add(new VariableBinding(new OID(oid)));
            }
            
            pdu.setType(PDU.GET);

            ResponseEvent event = snmp.send(pdu, target);
            if (event != null) {
                PDU responsePDU = event.getResponse();
                if (responsePDU != null) {
                	for (int i = 0 ; i < OIDS.length; i++) {
                		VariableBinding vb = event.getResponse().get(i);
                		String results = vb.getVariable().toString();
                		res[i] = HextoCC(results);
                	}
                }
                else return null;
            }
            else return null;
            snmp.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return res;
	}
	

	/**
	 * Create the frame.
	 */
	
	public SNMP_interface() {
		setTitle("PBL04_SNMP monitoring traffics");
		setForeground(new Color(0, 255, 0));
		setBackground(new Color(240, 240, 240));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 898, 617);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		TextField textField = new TextField();
		textField.setFont(new Font("NSimSun", Font.BOLD, 15));
		textField.setBounds(142, 10, 176, 25);
		contentPane.add(textField);
		
		Button button = new Button("GET PoLL");
		button.setBackground(new Color(0, 0, 0));
		
		button.setFont(new Font("NSimSun", Font.BOLD, 15));
		button.setForeground(new Color(0, 255, 64));
		button.setActionCommand("button1");
		button.setBounds(336, 10, 102, 25);
		contentPane.add(button);
		
		TextArea textArea = new TextArea("Kết quả::\n\n\n", 20, 15);
		textArea.setEditable(false);
		textArea.setFont(new Font("Copperplate Gothic Light", Font.ITALIC, 14));
		textArea.setForeground(new Color(0, 255, 64));
		textArea.setBackground(new Color(0, 0, 0));
		textArea.setBounds(28, 56, 617, 383);
		contentPane.add(textArea);
		
		Label label = new Label("Nhap IP agent");
		label.setForeground(new Color(0, 255, 0));
		label.setFont(new Font("NSimSun", Font.BOLD, 15));
		label.setBounds(28, 13, 120, 22);
		contentPane.add(label);
		
		Button button_1 = new Button("Memory Monitoring Dial");
		
		button_1.setForeground(new Color(0, 255, 64));
		button_1.setFont(new Font("NSimSun", Font.BOLD, 15));
		button_1.setBackground(Color.BLACK);
		button_1.setActionCommand("button1");
		button_1.setBounds(655, 56, 206, 25);
		contentPane.add(button_1);
		
		Choice choice = new Choice();
		choice.setForeground(new Color(255, 0, 255));
		choice.setFont(new Font("NSimSun", Font.BOLD, 15));
		choice.setBounds(540, 10, 176, 20);
		contentPane.add(choice);
		
		Button button_1_1 = new Button("Storage Dial");
		
		button_1_1.setForeground(new Color(0, 255, 64));
		button_1_1.setFont(new Font("NSimSun", Font.ITALIC, 15));
		button_1_1.setBackground(Color.BLACK);
		button_1_1.setActionCommand("button1");
		button_1_1.setBounds(759, 10, 102, 25);
		contentPane.add(button_1_1);
		
		Button button_1_1_1 = new Button("CPU-Traffics monitoring Dial");
		
		button_1_1_1.setForeground(new Color(0, 255, 64));
		button_1_1_1.setFont(new Font("NSimSun", Font.BOLD, 15));
		button_1_1_1.setBackground(Color.BLACK);
		button_1_1_1.setActionCommand("button1");
		button_1_1_1.setBounds(651, 98, 210, 25);
		contentPane.add(button_1_1_1);
		
		
		
		
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dem++;
				//textArea.setText(textField.getText());
				String agent = "udp:" + textField.getText().toString() + "/161";
				
				String community = "public";
				String sysnameOID = SystName;
				String[] oids = {
					SystName, SystLocation, SystContact, 
					SystUptime, PC, hrMemorySize, 
					 hrProcessorLoad, hrSystemProcesses,
					Input_bandwidth, ipInReceies
				};
				
				TransportMapping<?> transport;
				try {
					transport = new DefaultUdpTransportMapping();
					transport.listen();
					Snmp snmp = new Snmp(transport);
					PDU pdu = new PDU();
					for (String oid : oids) {
		                pdu.add(new VariableBinding(new OID(oid)));
		            }
					pdu.setType(PDU.GET);
					//pdu.add(new VariableBinding(new OID(sysnameOID)));
					CommunityTarget target = new CommunityTarget();
					target.setCommunity(new OctetString(community));
			        target.setAddress(GenericAddress.parse(agent));
			        target.setVersion(SnmpConstants.version2c);
			        //System.out.println(getMAXDevice_Index(target, snmp));
			        
			        
			        System.out.println(getMAXNum_Interfaces(agent, community));
			        String[] nameINF = GetFuLLName_Interface(agent, community);
			        int[] allo = GetFuLL_hrStorageAllocationUnits(agent, community);
			        String[] nameSto = GetFuLLName_Storage(agent, community);
			        double[] stoSize = GetFuLL_hrStorageSize(agent, community);
			        double[] stoUsed = GetFuLL_hrStorageUsed(agent, community);
			        int[] allprocessorLoad = getFuLL_hrProcessorLoad(agent, community);
			        
			        
			        
			        for (int i = 0 ; i < getMAXNum_Interfaces(agent, community) ; i++) {
			        	System.out.println(nameINF[i]);
			        }
			        
			        
			        
			        System.out.println(getMAXNum_hrStorageIndex(agent, community));
			        
			        for (int i = 0 ; i < getMAXNum_hrStorageIndex(agent, community) ; i++) {
			        	System.out.println(nameSto[i]);
			        }
			        
			        
			        
			        DecimalFormat dfZ = new DecimalFormat("#.##");
			        
			        for (int i = 0 ; i < getMAXNum_hrStorageIndex(agent, community) ; i++) {
			        	System.out.println(dfZ.format(stoSize[i]));
			        }
			        
			        
			        for (int i = 0 ; i < getMAXNum_hrStorageIndex(agent, community) ; i++) {
			        	System.out.println(dfZ.format(stoUsed[i]));
			        }
			        System.out.println("\n\n");
			        double CPUZ = 0;
			        for (int i = 0 ; i < num_Logic_Processors ; i++)
			        {
			        	CPUZ += allprocessorLoad[i];
			        	System.out.println(Integer.toString(allprocessorLoad[i]));
			        }
			        CPUZ = CPUZ / num_Logic_Processors;
			        ResponseEvent response = snmp.get(pdu, target);
			        if (response != null && response.getResponse() != null) {
			        	//VariableBinding vb = response.getResponse().get(0);
						if(dem < 10)
			        		textArea.append("\n         Device @0" + Integer.toString(dem) + "   " + agent +  " @info:\n");

						else textArea.append("\n         Device @" + Integer.toString(dem) + "   " + agent +  " @info:\n");
						
						DecimalFormat df = new DecimalFormat("#.##");
						for (int i = 0 ; i < oids.length; i++) {
							
							VariableBinding vb = response.getResponse().get(i);
							if (oids[i] == ".1.3.6.1.2.1.25.3.3.1.2.7")
								textArea.append( ".1.3.6.1.2.1.25.3.3.1.2..." +  ": CPU đang chiếm trung bình: " + df.format(CPUZ) + "% trong 1 phút qua\n");
						
							else if (oids[i] == ".1.3.6.1.2.1.25.2.2.0") 
								textArea.append(oids[i] + ":  RAM " + df.format(Double.parseDouble(vb.getVariable().toString()) / 1024 / 1024) + " gb\n");	
						
							else if (oids[i] == ".1.3.6.1.2.1.25.1.6.0")
								textArea.append(oids[i] + ": Số tiến trình đang chạy: " + vb.getVariable().toString() + "\n");
							
							else if (oids[i] == ".1.3.6.1.2.1.2.2.1.5.2")
								textArea.append(oids[i] + ":  Băng thông vào của Interface02= " + df.format(Double.parseDouble(vb.getVariable().toString()) / 1000 / 1000) + " mbps\n");
							
							else textArea.append(oids[i] + ":   " + vb.getVariable().toString() + "\n");
						
						}
						textArea.append("Có " + getMAXNum_Interfaces(agent, community) + " interfaces. Tên các interface là::\n");
						for (int i = 0 ; i < getMAXNum_Interfaces(agent, community) ; i++) {
							textArea.append("\n"+Integer.toString(i+1) + ".  "+nameINF[i] + "");
						}
						textArea.append("\n\n");
						textArea.append("Có " + getMAXNum_Interfaces(agent, community) + " loại bộ nhớ.  Thông tin của chúng như sau::\n\n");
						for(int i = 0 ; i < getMAXNum_hrStorageIndex(agent, community) ; i++) {
							textArea.append("\n"+Integer.toString(i+1) + ".  "+nameSto[i] + ":");
							choice.add(nameSto[i]);
							textArea.append("\n\n Tổng dung lượng: " + df.format(stoSize[i]) + "gb\n");
							textArea.append("Dung lượng còn trống: " + df.format(stoSize[i] - stoUsed[i]) + "gb\n");
							textArea.append("Tỉ lệ đã sử dụng: " + df.format((stoUsed[i]/stoSize[i]) * 100) + "%\n\n");
						}
						
						textArea.append("\n\n\n\n");
			        }
			        else {
						if (dem < 10)
			        		textArea.append("SysName @0" + Integer.toString(dem) + "   "  + agent +  ": No response!\n");
						else textArea.append("SysName @" + Integer.toString(dem) + "   "  + agent +  ": No response!\n");
			        }
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String agent = "udp:" + textField.getText().toString() + "/161";
				String community = "public";
				testing.javaswingdev.gauge.GaugeChart gaugeChart1;
				
				
				
				
		        double memused = 0.0F;
		        double stototal = 0.0F;
		        double memUtilzation = 0.0F;
		        DecimalFormat df = new DecimalFormat("#.##");
				
				JFrame newFrame = new JFrame("Memory Monitor Traffics");
				newFrame.getContentPane().setBackground(Color.decode("#C6ded7"));
                newFrame.setSize(550, 400);
                newFrame.setLayout(new FlowLayout());
                
                
                
                newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
             	gaugeChart1 = new testing.javaswingdev.gauge.GaugeChart();
                gaugeChart1.setFont(new Font("Tw Cen MT", Font.PLAIN, 12));
                
                gaugeChart1.setMaxValue(100.0F);
                gaugeChart1.setMinValue(0.0F);
                gaugeChart1.setThresholdIndicator(50.0F);
                gaugeChart1.setTrackStart(70.0F);
                gaugeChart1.setTrackStop(100.0F);
				try {
					String[] nameSto = GetFuLLName_Storage(agent, community);
					double[] stoSize = GetFuLL_hrStorageSize(agent, community);
			        double[] stoUsed = GetFuLL_hrStorageUsed(agent, community);
			        for (int i = 0 ; i < nameSto.length ; i++) if (nameSto[i].equals("Physical Memory")) {
			        	memused = stoUsed[i];
			        	stototal = stoSize[i];
			        	break;
			        }
			        memUtilzation = (memused / stototal) * 100;
			        
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				gaugeChart1.setTitle("RAM Utilization");
                gaugeChart1.setValueAnimate((float)memUtilzation);
                
                
                Label lb1 = new Label();
                lb1.setFont(new Font("Tw Cen MT", Font.BOLD, 16));
                lb1.setText("Total memory: " + df.format(stototal) + " gb");
                lb1.setBounds(0, 100, 200,20);
                Label lb2 = new Label();
                lb2.setFont(new Font("Tw Cen MT", Font.BOLD, 16));
                lb2.setText("Memory in use: " + df.format(memused));
                lb2.setBounds(300, 100, 200,20);
                
                newFrame.getContentPane().add(lb1);
                newFrame.getContentPane().add(gaugeChart1);
                newFrame.getContentPane().add(lb2);
                newFrame.setVisible(true);
		}});
		
		button_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testing.javaswingdev.gauge.GaugeChart gaugeChart1;
				String agent = "udp:" + textField.getText().toString() + "/161";
				
				String community = "public";
				
				if (choice.getItemCount() <= 0) JOptionPane.showMessageDialog(null, "ComboBox không có Item (không có Storage) để hiển thị!!");
				else {
					JFrame newFrame = new JFrame("Dial Monitor Traffics");
					newFrame.getContentPane().setBackground(Color.decode("#C6ded7"));
	                newFrame.setSize(400, 400);
	                newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	             	gaugeChart1 = new testing.javaswingdev.gauge.GaugeChart();
	                gaugeChart1.setFont(new Font("Tw Cen MT", Font.PLAIN, 12));
	                newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Đóng cửa sổ mới khi đóng nó
	                
	                gaugeChart1.setMaxValue(100.0F);
	                gaugeChart1.setMinValue(0.0F);
	                gaugeChart1.setThresholdIndicator(50.0F);
	                gaugeChart1.setTrackStart(70.0F);
	                gaugeChart1.setTrackStop(100.0F);
	                //gaugeChart1.setValueAnimate((float)10);
	                int index = -1;
	                float getUtilization = 0;
	                String choiced = choice.getSelectedItem();
                //System.out.println(choiced);
                try {
					String[] nameSto = GetFuLLName_Storage(agent, community);
					double[] usedSto = GetFuLL_hrStorageUsed(agent, community);
					double[] sizeSto = GetFuLL_hrStorageSize(agent, community);
					int n = getMAXNum_hrStorageIndex(agent, community);
					for (int i = 0 ; i < n ; i++) if (nameSto[i].equals(choiced)) {
						 getUtilization = (float)((usedSto[i]/sizeSto[i]) * 100);
						 //System.out.println(Double.toString((usedSto[i]/sizeSto[i]) * 100));
						 index = i + 1;
						 break;
					}
							
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                gaugeChart1.setTitle("hrStorageIndex " + Integer.toString(index));
                gaugeChart1.setValueAnimate(getUtilization);
                gaugeChart1.setBounds(0, 0, 300, 300);
                
                //gaugeChart1.setBounds(0, 0, 150, 150);
                newFrame.getContentPane().add(gaugeChart1);
                
                
                newFrame.setVisible(true);
			}}
				//JOptionPane.showMessageDialog(null, "dsfdf");
				
		});
		
		
		button_1_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//JOptionPane.showMessageDialog(null, "HeSoLo");
				
				String agent = "udp:" + textField.getText().toString() + "/161";
				
				String community = "public";
                testing.javaswingdev.gauge.GaugeChart[] charts = new testing.javaswingdev.gauge.GaugeChart[num_Logic_Processors];
                if (num_Logic_Processors == 0) JOptionPane.showMessageDialog(null, "Chưa khởi tạo SNMP poll!");
                else {
                	JFrame newFrame = new JFrame("CPU Monitor Traffics");
					newFrame.getContentPane().setBackground(Color.decode("#C6ded7"));
	                newFrame.setSize(1500, 750);
	                newFrame.setLayout(new FlowLayout());
	                newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                	try {
    					int[] processorLoads = getFuLL_hrProcessorLoad(agent, community);  
    					for (int i = 0 ; i < num_Logic_Processors ; i++) {
    						charts[i] = new testing.javaswingdev.gauge.GaugeChart();
    						charts[i].setFont(new Font("Tw Cen MT", Font.PLAIN, 12));
    						charts[i].setMaxValue(100.0F);
    			            charts[i].setMinValue(0.0F);
    			            charts[i].setThresholdIndicator(50.0F);
    			            charts[i].setTrackStart(70.0F);
    			            charts[i].setTrackStop(100.0F);
    			            charts[i].setTitle("hrProcessor # " + Integer.toString(i+1));
    		                charts[i].setValueAnimate(processorLoads[i] + .0F);
    		                //charts[i].setBounds(0, 0, 400, 400);
    		                newFrame.getContentPane().add(charts[i]);
    					}
    					
    				} catch (IOException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}
                	newFrame.setVisible(true);
                	
                	
                }
			}
		});
	}
}