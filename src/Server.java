import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server extends UnicastRemoteObject implements IServer {
		public Map<String, ICliente> clientes = new  HashMap<String, ICliente>();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Server() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			try{
				IServer server= new Server();
				LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
				Naming.rebind("Server", server);
				System.out.println("Chat inicializado");
			}catch(Exception e){
				System.out.println(args[0]);
				e.printStackTrace();
			}
	}

	public boolean trataComando(int comando, String mensagem, String remetente, String destinatario) throws RemoteException{
		//Mensagem msg = new Mensagem(mensagem, remetente, destinatario);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat hourFormat = new SimpleDateFormat("kk");
		SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
		boolean flag=false;
		switch (comando){
		case 1:
			for(String cliente: clientes.keySet()){
				if(!cliente.equals(remetente)){
					
					clientes.get(cliente).getMensagem(remetente+" "+mensagem+" "+hourFormat.format(new Date())+"h"+minuteFormat.format(new Date())+"-"+dateFormat.format(new Date() ));
				}				
			}
			break;
		case 2: 
			if(clientes.containsKey(destinatario)){
				clientes.get(destinatario).getMensagem(remetente+" "+mensagem+" "+hourFormat.format(new Date())+"h"+minuteFormat.format(new Date())+"-"+dateFormat.format(new Date() ));
			}else{
				clientes.get(remetente).getMensagem("O usuário informado não existe.");
			}
			break;
		case 3:
			String participantes="";
			for(String cliente: clientes.keySet()){
				
					participantes+=cliente+"\n";
								
			}
			//System.out.println("Nome recebido --- "+ remetente);
			clientes.get(remetente).getMensagem(participantes);
			break;
		case 4:
			if(!clientes.containsKey(mensagem)){
				clientes.put(mensagem, clientes.get(remetente));
				clientes.remove(remetente);
				clientes.get(mensagem).getMensagem("Seu nome foi mudado para "+mensagem);
				flag= true;
			}else{
				//System.out.println("Nome recebido --- "+ remetente);
				clientes.get(remetente).getMensagem("O usuário informado já existe");
			}
			break;
		case 5:
			
			clientes.remove(remetente);
			break;
			
		}
		//System.out.println("Seu nome mudou para "+nome);
		
		return flag;
		
	}
	@Override
	public boolean cadastraCliente(String nome, ICliente cliente) throws RemoteException {
		// TODO Auto-generated method stub
		if(!clientes.containsKey(nome)){
			clientes.put(nome, cliente);
			return true;
		}else{
			return false;
		}
		
	}

}
