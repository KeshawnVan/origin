package other;

import org.junit.Test;

import javax.script.*;

public class TestScript {

    private ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");

    @Test
    public void hello() throws Exception {
        scriptEngine.eval("print('Hello world')");
    }

    @Test
    public void func() throws Exception {
        scriptEngine.eval("var fun1 = function(name) {\n" +
                "    print('Hi there from Javascript, ' + name);\n" +
                "    return \"greetings from javascript\";\n" +
                "};");
        Invocable invocable = (Invocable) scriptEngine;
        Object result = invocable.invokeFunction("fun1", "origin");
        System.out.println(result);
        System.out.println(result.getClass());
    }

    @Test
    public void sql() throws Exception {
        Bindings bindings = new SimpleBindings();
        bindings.put("name", "fkx");
        bindings.put("age", "23");
        bindings.put("code", null);
        Object eval = scriptEngine.eval(
                "var sql = 'select * from customer where name = ? ';" +
                        "if(age != null) {sql = sql + 'and age = ? '} ;" +
                        "if(code != null){sql = sql + 'and code = ?'}" +
                        "sql", bindings);
        System.out.println(eval);
    }
}
