import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;



public class Bot extends TelegramLongPollingBot {
    TaskManager taskManager = new TaskManager();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage().hasText() && update.hasMessage()) {
            String message = update.getMessage().getText();
            long id = update.getMessage().getFrom().getId();
            if (message.equals("/start")) {
                sendStartMessage(id);
            } else if (message.startsWith("/add")) {
                String[] splitMessage = message.replace("/add ", "").split(",");
                if (splitMessage.length >= 2) {
                    String name = splitMessage[0];
                    String taskText = splitMessage[1];
                    taskManager.addTask(name, taskText, id);
                } else {
                    sendMessage(id, "⚠️ ***Ошибка! Используйте запятую как разделитель.***\nПример: `/add Покупки, Купить хлеб и молоко` ");
                }
            } else if (message.equals("/list")) {
                sendMessage(id, taskManager.showTasks(id));
            } else if (message.startsWith("/done")) {
                String messageWithoutCommand = message.replace("/done ", "");
                int integerFromMessage = Integer.parseInt(messageWithoutCommand);
                sendMessage(id, taskManager.deleteTask(integerFromMessage, id));
            } else if (message.equals("/properties")) {
                sendPropertiesMessage(id);
            } else if (message.startsWith("/edit")) {
                try {
                    String[] split_message = message.replace("/edit ", "").split(",",3);
                    if (split_message.length >= 3) {
                        int mess_id = Integer.parseInt(split_message[0].trim());
                        String name = split_message[1].trim();
                        String task = split_message[2].trim();
                        if (name.isEmpty() || task.isEmpty()) {
                            throw new Exception();
                        }
                        sendMessage(id, taskManager.editFullTask(name,task, mess_id, id));
                    } else {
                        sendMessage(id, "⚠️ Нужно ввести ID, задачу и новое название через запятую!\nПример: `/edit 15, Купить молоко, в магазине купить молоко` ");
                    }
                } catch (NumberFormatException e ) {
                    sendMessage(id, "❌ Ошибка: ID должен быть числом!");
                } catch (Exception e) {
                    sendMessage(id, "❌ Что-то пошло не так при редактировании.");
                }
            }

        }
    }

    @Override
    public String getBotUsername() {
        return System.getenv("BOT_NAME");
    }

    @Override
    public String getBotToken() {
        return System.getenv("BOT_TOKEN");
    }

    private void sendStartMessage(long id) {
        String message = "\uD83D\uDC4B Привет! Я твой персональный менеджер задач.\n" +
                "\n" +
                "Я помогу тебе ничего не забыть и держать дела в порядке. Все твои задачи сохраняются в надежную базу данных PostgreSQL.\n" +
                "\n" +
                "Как правильно пользоваться командами ты можешь узнать в /properties.\n" +
                "\n" +
                "Что я умею: \uD83D\uDCDD /add [название] , [описание] — добавить новую задачу \uD83D\uDCCB /list — показать список всех дел и их id ✅ /done [id] — отметить задачу как выполненную, задача удаляется из списка\n" +
                "\n" +
                "Давай запишем твою первую задачу прямо сейчас? /add ";
        sendMessage(id, message);
    }
    private void sendPropertiesMessage(long id) {
        String message = "\uD83D\uDC4B Привет! Я твой персональный менеджер задач.\n" +
                "\n" +
                "Добавление - чтобы добавление задачи прошло успешно, вам нужно добавлять задачи в таком формате `/add Название задачи, задача.`\n" +
                "\n" +
                "Показать список - чтобы вам показался список с вашими задачами и их id просто введите `/list.`\n" +
                "\n" +
                "Удаление задач - удалить задачу нужно именно по id который указан у неё рядом в списке, в таком формате `/done id(В виде цифры)`\n"
                + "\n" +
                "Изменение задач - нужно ввести ID, задачу и новое название через запятую!\nПример: `/edit 15, Купить молоко, в магазине купить молоко` ";
        sendMessage(id, message);
    }
    public void sendMessage(long id, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(message);
        sendMessage.setChatId(id);
        sendMessage.setParseMode("Markdown");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
