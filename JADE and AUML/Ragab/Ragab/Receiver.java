import jade.Boot;
import jade.core.Agent;
import javax.swing.JOptionPane;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;


public class Receiver extends Agent {
  protected void setup(){
    System.out.println("Hi I'm "+getAID().getLocalName());
    this.registerService();
    addBehaviour(new receiveWelcome());
  }

  private void registerService(){
    DFAgentDescription dfd = new DFAgentDescription();
    dfd.setName(this.getAID());

    ServiceDescription sd = new ServiceDescription();
    sd.setType("Receiver");
    sd.setName("Receiver");
    dfd.addServices(sd);
    try{
      DFService.register(this,dfd);
    }
    catch( FIPAException e){
      System.out.println("Can't register service.");
    }

  }

  private class receiveWelcome extends CyclicBehaviour{
    public void action(){
      ACLMessage msg= myAgent.receive();
      if(msg!=null){
        System.out.println(
                                      "\nI received a message from " + msg.getSender() +
                                      " telling me that " + msg.getContent());
        ACLMessage reply = msg.createReply();
        reply.setContent("thank you " + msg.getSender());
        send(reply);
      }
    }
  }
  protected void takeDown(){
    System.out.println(getAID().getLocalName()+" say good bye :)");
  }
}