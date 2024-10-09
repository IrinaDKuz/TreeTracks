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


    public final static Map<String, String> MESSAGE_TYPE_MAP = new HashMap<>() {
        {
            put("message", "Message");
            put("info", "Info");
            put("notice", "Notice");
            put("warning", "Warning");
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

    public static final String[] TASK_WORDS_REASON = {
            "Waiting for approval", "Resource unavailability", "Pending feedback", "Client request", "Technical issue",
            "Reprioritized", "Lack of budget", "Team capacity", "Scope change", "Data dependency",
            "Third-party delay", "Insufficient information", "Testing phase", "Review pending", "Legal clearance",
            "Internal discussion", "Unexpected outage", "Waiting for updates", "Dependencies unresolved", "Compliance check",
            "Market conditions", "Team restructuring", "New requirements", "Shift in priorities", "Stakeholder delay",
            "Awaiting documentation", "Cross-team alignment", "Change request", "Prototype failure", "Incomplete assets",
            "Pending training", "Vendor delay", "Quality assurance", "Internal conflict", "Waiting on permissions",
            "Unexpected event", "Leadership approval", "Contract issues", "Risk assessment", "Platform instability",
            "Regulatory review", "Software upgrade", "Pending resources", "Holiday period", "Budget constraints",
            "System downtime", "Pending clarification", "External audit", "Awaiting legal review", "Team realignment"
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(calendar.getTime());
    }

    public static String generateDueDatePlusNDays(int n) {
        // Получаем текущее время
        Calendar calendar = Calendar.getInstance();
        // Добавляем 3 дня к текущей дате
        calendar.add(Calendar.DATE, n);

        // Форматируем дату в нужный формат
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(calendar.getTime());
    }

}
