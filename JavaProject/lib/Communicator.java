package Environment;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import simplifiedSimulator.QueyCrane;
import simplifiedSimulator.State;
import simplifiedSimulator.Task;

public class Communicator {
	
	public ServerSocket server;
	public Socket client;
	public PrintWriter out;
	public InputStream in;
	public int portNum; 
	public boolean isConnected = false;
	public String type;
	
	private DecimalFormat df = new DecimalFormat("0.00");

	
	public Communicator(int portNum, String type) {
		this.portNum = portNum;
		this.type = type;
	}
	
	public void startServer() throws IOException {
		server = new ServerSocket(portNum);
		client = server.accept();
		
		//System.out.println(client.getInetAddress().getHostAddress() + " connected!");
		
		out = new PrintWriter( client.getOutputStream(), true );
		//in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		in = client.getInputStream();
		
		isConnected = true;
	}
	
	public void close() throws IOException {
		server.close();
		client.close();
	}
	
	public void sendEnvInfo(State state, double reward) throws JSONException {
		/*
		double[] truckToQCDistance = state.truckToQCDistance;  // static distance 
		int[] qcRemainTaskAmount = state.qcRemainTaskAmount;
		int[] qcTruckQueueLength = state.qcTruckQueueLength;
		//double[] qcUtilization = info.qcUtilization;
		int[] currentWorkingTruckAmount = state.currentWorkingTruckAmount;
		int shipAmount = state.shipAmount;
		//ArrayList<Double> operationTimes = state.taskOperationTimes;
		boolean isDone = state.isDone;
		*/
		// send json object (state, reward, isDone)
		
		JSONObject json = new JSONObject();
		json.put("truckToQCDistance", state.truckToQCDistance);
		json.put("qcRemainTaskAmount", state.qcRemainTaskAmount);
		json.put("qcTruckQueueLength", state.qcTruckQueueLength);
		json.put("currentWorkingTruckAmount", state.currentWorkingTruckAmount);
		json.put("headingToQCAmount", state.headingToQCAmount);
		json.put("qcTypes", state.qcTypes);
		json.put("shipAmount", state.shipAmount);
		//json.put("operationTimes", operationTimes);
		json.put("reward", reward);
		json.put("isDone", state.isDone);
		out.println(json.toString()); // send to client
		
		//System.out.println(type+" Server sent state to port "+portNum);
	}
	
	public void sendEndInfo(State state) throws JSONException {
		
		JSONObject json = new JSONObject();
		for (int i=0;i<state.shipAmount*4;i++) {
			String text = " ";
			for (Task t : state.queyCranes.get(i).completedTaskList) {
				text += df.format(t.waitTime)+ " ";
			}
			json.put(String.valueOf(i), text.trim());
		}
		out.println(json.toString()); // send to client
		//System.out.println(json.toString());
		
	}
	
	
	public int getAction() throws IOException {
		// get action
		//int action = Integer.parseInt(in.readLine());
		//System.out.println(type+" Server Waiting for Action at port: "+portNum);	

		int action;
		byte[] buffer = new byte[1024];
		int len = in.read(buffer);
		action = Integer.parseInt(new String(buffer, 0, len));
		
		//System.out.println(type+" Server Received Action: " +action+" at port: "+portNum);	
		return action;
	}
	
	
	
	
}	
