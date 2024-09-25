package Helper;

import AdvertPackage.entity.AdvertPrimaryInfo;

import java.text.SimpleDateFormat;
import java.util.*;

import static SQL.AdvertSQL.getRandomValueFromBDWhere;

public class Tasks {

    public final static Map<String, String> TASKS_STATUS_MAP = new HashMap<>() {
        {
            put("draft", "Draft");
            put("new", "New");
           // put("additional_info_required", "Additional info required");
           // put("progress", "In progress");
           // put("conditions_updated", "Conditions updated");
          //  put("postponed", "Postponed");
          //  put("past_due", "Past due");
          //  put("resolved", "Resolved");
          //  put("cancelled", "Cancelled");

        }
    };

    public static final String[] TASK_WORDS = {
            "Project", "Analyze", "Plan", "Task", "Develop",
            "Goal", "Create", "Strategy", "Design", "Process",
            "Coordinate", "Solution", "Execute", "Team", "Organize",
            "Facilitate", "Proposal", "Assess", "Outcome", "Train",
            "Research", "Presentation", "Support", "Schedule", "Evaluate",
            "Monitor", "Documentation", "Innovate", "Insight", "Engagement",
            "Feedback", "Deliverable", "Vision", "Collaboration", "Action",
            "Protocol", "Adjust", "Team", "Initiative", "Negotiate",
            "Improve", "Resource", "Framework", "Model", "Engage",
            "Align", "Enhance", "Test", "Validate", "Conduct",
            "Prepare", "Transform", "Communicate", "Analysis", "Strategize",
            "Document", "Innovative", "Metrics", "Challenge", "Campaign",
            "Support", "Performance", "Iteration", "Risk", "Standard",
            "Benchmark", "Skill", "Environment", "Scope", "Insight",
            "Feedback", "Refine", "Mobilize", "Forecast", "Review",
            "Connect", "Champion", "Revise", "Experiment", "Outcome",
            "Study", "Network", "Synthesize", "Resource", "Engage",
            "Objective", "Enhance", "Budget", "Collaboration", "Pathway",
            "Initiate", "Creative", "Integrate", "Facilitate", "Innovate",
            "Expand", "Focus", "Journey", "Envision", "Prioritize"
    };

    public static String generateDueDatePlusFiveMinutes() {
        // Получаем текущее время
        Calendar calendar = Calendar.getInstance();

        // Добавляем 5 минут
        calendar.add(Calendar.MINUTE, 5);

        // Форматируем дату в нужный формат
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(calendar.getTime());
    }

    public static String generateDueDate() {
        // Получаем текущее время
        Calendar calendar = Calendar.getInstance();
        // Форматируем дату в нужный формат
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        return dateFormat.format(calendar.getTime());
    }

}
