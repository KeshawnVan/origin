package star.utils;

import java.util.Map;


public final class StackUtil {

    public static String getCurrentThreadStack() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        try {
            for (Map.Entry<Thread, StackTraceElement[]> threadEntry : Thread.getAllStackTraces().entrySet()) {
                if (threadEntry.getKey().equals(Thread.currentThread())) {
                    StackTraceElement[] stackTraceElements = threadEntry.getValue();
                    stringBuilder.append(Thread.currentThread()).append(" stack is : ").append("\n");
                    for (StackTraceElement stackTraceElement : stackTraceElements) {
                        stringBuilder.append("\t").append(stackTraceElement).append("\n");
                    }
                }
            }
        } catch (Exception e) {
        }
        return stringBuilder.toString();
    }

    public static String getAllThreadStack() {
        StringBuilder stringBuilder = new StringBuilder().append("\n");
        try {
            for (Map.Entry<Thread, StackTraceElement[]> threadEntry : Thread.getAllStackTraces().entrySet()) {
                Thread thread = threadEntry.getKey();
                StackTraceElement[] stackTraceElements = threadEntry.getValue();
                stringBuilder.append(thread).append(" stack is : ").append("\n");
                for (StackTraceElement stackTraceElement : stackTraceElements) {
                    stringBuilder.append("\t").append(stackTraceElement).append("\n");
                }
            }
        } catch (Exception e) {
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        System.out.println(getCurrentThreadStack());
    }
}
