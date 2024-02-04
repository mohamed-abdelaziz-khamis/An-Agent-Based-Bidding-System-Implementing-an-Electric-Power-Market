import javax.swing.JOptionPane;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;

import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.core.AID;

import java.util.Iterator;

public class Sender extends Agent {

  DFAgentDescription[] result;
  AID[] aids;
  protected void setup() {
    System.out.println("Hi I'm " + getAID().getLocalName());

    addBehaviour(new WakerBehaviour(this, 6000) {
      public void handleElapsedTimeout() {
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Receiver");
        dfd.addServices(sd);

        try {
          result = DFService.search(myAgent, dfd);
          aids = new AID[result.length];
          for (int i = 0; i < result.length; i++) {
            aids[i] = result[i].getName();
          }
        }
        catch (Exception e) {
          System.out.println("Can't find any Reciver");
        }

        myAgent.addBehaviour(new sendName());
      }
    });
    addBehaviour(new receiveReply());
  }

  private class sendName extends OneShotBehaviour {

    public void action() {
      ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
      for (int i = 0; i < aids.length; i++) {
        msg.addReceiver(aids[i]);
        msg.setLanguage("English");
        msg.setContent(getAID().getName());
        send(msg);
      }
    }
  }
  private class receiveReply
      extends CyclicBehaviour {
    public void action() {
      ACLMessage msg = myAgent.receive();
      if (msg != null) {
	System.out.println("");
        System.out.println(
                                      "I received a message from " +
                                      msg.getSender() +
                                      " telling me that " + msg.getContent());
      }
    }
  }

  protected void takeDown() {
    System.out.println(
                                  getAID().getLocalName() + " say good bye :)");
  }

}