package server;

import chess.services.GlobalContext;
import chess.util.Environment;
import chess.util.Move;
import com.google.gson.Gson;
import lombok.Setter;
import org.springframework.boot.json.GsonJsonParser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
public class FrontController {

    private Gson g = new Gson();
    private Socket socket;
    private static Map<String, Method> headerToMethodMap;

    public void dispatchMessage(String msg) {
        String header = msg.substring(0, msg.indexOf('\n'));
        String body = msg.substring(msg.indexOf('\n'));

        if (!Protocol.headerToClassMapping.containsKey(header)) {
            System.out.println("Unknown header " + header);
            return;
        }

        Class<?> bodyClass = Protocol.headerToClassMapping.get(header);

        Object bodyData = null;

        if (bodyClass != null) {
            bodyData = g.fromJson(body, bodyClass);
        }

        try {
            Method method = headerToMethodMap.get(header);
            Object result = bodyData != null ? method.invoke(this, bodyData) : method.invoke(this);
            String responseBody = g.toJson(result);
            sendResponse(responseBody);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    static {
        headerToMethodMap = Arrays.stream(FrontController.class.getMethods()).filter(x -> x.isAnnotationPresent(Handles.class))
                .collect(Collectors.toMap(x -> x.getAnnotation(Handles.class).value(), x -> x));
    }

    private void sendResponse(String response) {
        try {
            socket.getOutputStream().write(response.getBytes(StandardCharsets.UTF_8));
            socket.getOutputStream().flush();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    @Handles(Protocol.PLAY_MOVE)
    public Environment playMove(Move move) {
        System.out.println(move);
        return GlobalContext.getEnvironment();
    }

    @Handles(Protocol.NEW_GAME)
    public String newGame() {
        System.out.println("New game requested");
        return "Success new game";
    }

    @Handles(Protocol.SAVE_GAME)
    public String saveGame() {
        System.out.println("Save game requested");
        return "Success save game";
    }

    @Handles(Protocol.ENVIRONMENT_STATE)
    public Environment environmentState() {
        return GlobalContext.getEnvironment();
    }

}
