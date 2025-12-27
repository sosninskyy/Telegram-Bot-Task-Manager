
import java.sql.*;


public class TaskManager {
    private final Connection connection;

    public TaskManager() {
        this.connection = DbManager.getConnection();
    }


    public String addTask(String taskName, String task, long id) {
        String sql = "INSERT INTO TASK (task_name, task, telegram_id) VALUES (?, ?, ?)";
        int result = 0;
        if (connection != null) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, taskName);
                preparedStatement.setString(2, task);
                preparedStatement.setLong(3, id);
                result = preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (result > 0) {
            return "Задача " + taskName + " успешно добавлена ✅";
        } else {
            return "Задача не добавлена, узнайте как сохранять задачи через /properties";
        }
    }

    public String showTasks(long id) {
        String sql = "SELECT id,task_name, task FROM TASK WHERE telegram_id = ?";
        StringBuilder sb = new StringBuilder("📋 **Ваши задачи:**\n\n");
        boolean hasTask = false;
        if (connection != null) {
            try (PreparedStatement pr = connection.prepareStatement(sql)){
                pr.setLong(1, id);
                try (ResultSet resultSet = pr.executeQuery()) {
                    while (resultSet.next()) {
                        hasTask = true;
                        String name = resultSet.getString("task_name");
                        String task = resultSet.getString("task");
                        long id_sql = resultSet.getLong("id");

                        sb.append("• id ").append(id_sql).append(" - ").append(name).append(": ").append(task).append("\n");
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return hasTask ? sb.toString() : "У вас пока нет активных задач. ️";
    }

    public String deleteTask(int task_id,long id) {
        String sql = "DELETE FROM TASK WHERE id = ? AND telegram_id = ?";
        int result = 0;
        if (connection != null) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, task_id);
                preparedStatement.setLong(2, id);
                result = preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (result > 0) {
            return "Задача под id " + task_id + " успешно удалена ✅";
        } else {
            return "Данной задачи не существует❌️";
        }
    }

    public String editFullTask(String taskName,String task, int task_id, long id) {
        String sql = "UPDATE TASK SET task_name = ?,task = ? WHERE id = ? AND telegram_id = ?";
        int result = 0;
        if (connection != null) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, taskName);
                preparedStatement.setString(2, task);
                preparedStatement.setInt(3, task_id);
                preparedStatement.setLong(4, id);
                result = preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (result > 0) {
            return "Задача " + taskName + " была изменена ✅";
        } else {
            return "Задача не изменена, узнайте как сохранять задачи через /properties";
        }
    }
}
