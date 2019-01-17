package projectReader;

import java.io.File;
import java.util.ArrayList;

public class FunctionPoint {
    private File location;
    private String type;
    private String name;
    private ArrayList<String> parameters = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getLocation() {
        return location;
    }

    public void setLocation(File location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean addParameter(String parameter){
        return parameters.add(parameter);
    }

    public boolean addParameter(ArrayList<String> params){
        return parameters.addAll(params);
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }

    @Override
    public int hashCode() {
        int newHash = 17;
        newHash = 31 * newHash + (this.location != null ? this.location.getAbsolutePath().hashCode() : 0)
                + (this.name != null ? this.name.hashCode() : 0)
                + (this.type != null ? this.type.hashCode() : 0);
        return newHash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!FunctionPoint.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final FunctionPoint other = (FunctionPoint) obj;
        return other.getName().equals(this.getName())
                && other.getLocation().getAbsolutePath().equals(this.getLocation().getAbsolutePath())
                && other.getType().equals(this.getType());
    }

}
