
import java.util.*;

interface EventListener {
    void update(String filename);
}

class EmailAlertsListener implements EventListener {
    private String email;

    public EmailAlertsListener(String email) {
        this.email = email;
    }

    @Override
    public void update(String filename) {
        System.out.println("Email enviado para " + email + ": O arquivo " + filename + " foi alterado.");
    }
}

class LoggingListener implements EventListener {
    private String logFile;

    public LoggingListener(String logFile) {
        this.logFile = logFile;
    }

    @Override
    public void update(String filename) {
        System.out.println("Log salvo em " + logFile + ": O arquivo " + filename + " foi alterado.");
    }
}

class EventManager {
    Map<String, List<EventListener>> listeners = new HashMap<>();

    public EventManager(String... operations) {
        for (String op : operations) {
            this.listeners.put(op, new ArrayList<>());
        }
    }

    public void subscribe(String eventType, EventListener listener) {
        List<EventListener> users = listeners.get(eventType);
        users.add(listener);
    }

    public void unsubscribe(String eventType, EventListener listener) {
        List<EventListener> users = listeners.get(eventType);
        users.remove(listener);
    }

    public void notify(String eventType, String data) {
        List<EventListener> users = listeners.get(eventType);
        for (EventListener listener : users) {
            listener.update(data);
        }
    }
}

class Editor {
    public EventManager events;
    private String filename;

    public Editor() {
        this.events = new EventManager("open", "save");
    }

    public void openFile(String filename) {
        this.filename = filename;
        System.out.println("Abrindo arquivo: " + filename);
        events.notify("open", filename);
    }

    public void saveFile() {
        if (this.filename != null) {
            System.out.println("Salvando arquivo: " + filename);
            events.notify("save", filename);
        } else {
            System.out.println("Nenhum arquivo aberto para salvar!");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Editor editor = new Editor();

        EmailAlertsListener emailListener = new EmailAlertsListener("user@example.com");
        LoggingListener logListener = new LoggingListener("log.txt");

        editor.events.subscribe("open", emailListener);
        editor.events.subscribe("save", logListener);

        editor.openFile("documento.txt");
        editor.saveFile();
    }
}
