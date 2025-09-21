import java.util.List;

public class SchemaObject {
    private String name;
    private List<String> fields;

    public String getName() {
        return name;
    }

    public List<String> getFields() {
        return fields;
    }

    public SchemaObject(String name, List<String> fields){
        this.name = name;
        this.fields = fields;
    }

    @Override
    public String toString(){
        return "SchemaObject{}" + "name = " + name + ", fields= " + fields + "}" ;
    }
}
