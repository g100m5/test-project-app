import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import org.json.JSONObject;

import java.io.IOException;

public class ConnectionBot {
    interface Command {
    }

    final static class Request implements Command {
        public final ActorRef<Response> bot;
        public final String message;

        public Request(ActorRef<Response> bot, String message) {
            this.message = message;
            this.bot = bot;
        }
    }

    final static class Response implements Command {
        public final ActorRef<Command> connection;
        public final String message;

        public Response(ActorRef<Command> connection, String message) {
            this.connection = connection;
            this.message = message;
        }
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(context -> new ConnectionBot(context).available());
    }

    private final ActorContext<Command> context;

    private ConnectionBot(ActorContext<Command> context) {
        this.context = context;
    }

    private Behavior<Command> available() {
        return Behaviors.receive(Command.class)
                .onMessage(Request.class, (msg) -> {
                    msg.bot.tell(new Response(context.getSelf(), makeRequest(msg.message)));
                    return Behaviors.same();
                })
                .build();
    }

    private String makeRequest(String message) throws IOException {
        JSONObject json = apiConnection.readJsonFromUrl("http://localhost:8085/ask?question=" + message);
        return json.get("content").toString();
    }

}
