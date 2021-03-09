import akka.NotUsed;
import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        ActorSystem.create(Main.create(), "ActorSystem-1");
    }

    private static Behavior<NotUsed> create() {
        return Behaviors.setup(context -> new Main(context).behavior());
    }

    private final ActorContext<NotUsed> context;

    private Main(ActorContext<NotUsed> context) {
        this.context = context;
    }

    private Behavior<NotUsed> behavior() {
        ActorRef<ConnectionBot.Command> connection = context.spawn(ConnectionBot.create(), "connection-1");
        ActorRef<Bot.Command> bot = context.spawn(Bot.create(connection), "bot-1");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            while (true) {
                String lineOne = reader.readLine();
                bot.tell(new Bot.UserMessage(lineOne));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Behaviors.empty();
    }
}
