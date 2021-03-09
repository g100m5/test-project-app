import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;

class Bot {

    interface Command {
    }

    enum Init implements Command {
        INSTANCE
    }

    final static class UserMessage implements Command {
        public final String message;

        public UserMessage(String message) {
            this.message = message;
        }

    }

    static class HandleConnectionResponse implements Command {
        final ConnectionBot.Response message;

        public HandleConnectionResponse(ConnectionBot.Response message) {
            this.message = message;
        }
    }

    public static Behavior<Command> create(ActorRef<ConnectionBot.Command> connection) {
        return Behaviors.setup(ctx -> new Bot(ctx, connection).init());
    }

    private final ActorContext<Command> context;
    private final ActorRef<ConnectionBot.Command> connection;
    private final ActorRef<ConnectionBot.Response> adapter;

    private Bot(ActorContext<Command> context,
                ActorRef<ConnectionBot.Command> connection) {
        this.context = context;
        this.connection = connection;
        this.adapter = context.messageAdapter(ConnectionBot.Response.class,
                HandleConnectionResponse::new);
    }

    private Behavior<Command> init() {
        System.out.println("Hi, can I help you? Please type (Y)es");
        return Behaviors.receive(Command.class)
                .onMessage(UserMessage.class, m -> m.message.toLowerCase().equals("y"), m -> waiting())
                .onMessage(UserMessage.class, m -> m.message.toLowerCase().equals("yes"), m -> waiting())
                .onMessage(UserMessage.class, m -> init())
                .build();
    }

    private Behavior<Command> waiting() {
        System.out.println("Please, write your question.");
        return Behaviors.receive(Command.class)
                .onMessage(UserMessage.class, m -> m.message.equals(""), m -> Behaviors.same())
                .onMessage(UserMessage.class, m -> {
                    connection.tell(new ConnectionBot.Request(adapter, m.message));
                    return getResponse();
                })
                .build();
    }

    private Behavior<Command> getResponse() {
        return Behaviors.receive(Command.class)
                .onMessage(HandleConnectionResponse.class, msg -> {
                            System.out.println(msg.message.message);
                            return waiting();
                        }
                )
                .build();
    }

}
