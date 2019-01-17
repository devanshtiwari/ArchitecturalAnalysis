package metrics;

public class MetricsPOJO {


    String projectName;
    double complexity;
    double incomingDepeConc;
    double outgoingDepConc;
    double dependencies;
    double fileSizeDeviation;
    double avgFileSize;
    int fileMaxSize;
    int numOfFiles;
    int numOfFunctions;
    double normalizedCyleEdges;
    double normalizedCyclePaths;
    double weightedDependencyCost;
    double weightedDependencyCostSQ;
    double fileCuttingNumber;
    double functionCuttingNumber;
    double fileGraphDensity;
    double functionGraphDensity;
    double QValue;
    double avgClusterDependency;

    //


    public String getHeaders() throws IllegalAccessException {
        String headers = "";
//        for(Field field:getClass().getDeclaredFields()){
//            headers += field.getName() + ",";
//        }
        headers = "projectName,complexity,incomingDependencyConcentration,outgoingDependencyConcentration,dependencies," +
        "fileSizeDeviation,avgFileSize,fileMaxSize,numOfFile,numOfFunctions,normalizedCycleEdges,normalizedCyclePaths," +
        "weightedDependencyCost,weightedDependencyCostSQ,fileCuttingNumber,functionCuttingNumber,fileGraphDensity,functionGraphDensity," +
        "QValue,AvgClusterDependency";
        return headers + "\n";
    }

    public String getValues() throws IllegalAccessException {
        String values = "";
//        for(Field field:getClass().getDeclaredFields()){
//            values += field.get(this) + ",";
//        }
        values = projectName + "," + complexity + "," + incomingDepeConc + "," + outgoingDepConc + "," + dependencies + ","
                + fileSizeDeviation + "," + avgFileSize + "," + fileMaxSize + "," + numOfFiles + ","
                + numOfFunctions + "," + normalizedCyleEdges + "," + normalizedCyclePaths + "," + weightedDependencyCost + ","
                + weightedDependencyCostSQ + "," + fileCuttingNumber + "," + functionCuttingNumber + "," + fileGraphDensity + ","
                + functionGraphDensity + "," + QValue + "," + avgClusterDependency;
        return values + "\n";
    }


    public double getQValue() {
        return QValue;
    }

    public void setQValue(double QValue) {
        this.QValue = QValue;
    }

    public double getAvgClusterDependency() {
        return avgClusterDependency;
    }

    public void setAvgClusterDependency(double avgClusterDependency) {
        this.avgClusterDependency = avgClusterDependency;
    }
    public double getFileGraphDensity() {
        return fileGraphDensity;
    }

    public void setFileGraphDensity(double fileGraphDensity) {
        this.fileGraphDensity = fileGraphDensity;
    }

    public double getFunctionGraphDensity() {
        return functionGraphDensity;
    }

    public void setFunctionGraphDensity(double functionGraphDensity) {
        this.functionGraphDensity = functionGraphDensity;
    }


    public double getNormalizedCyleEdges() {
        return normalizedCyleEdges;
    }

    public double getFileCuttingNumber() {
        return fileCuttingNumber;
    }

    public void setFileCuttingNumber(double fileCuttingNumber) {
        this.fileCuttingNumber = fileCuttingNumber;
    }

    public double getFunctionCuttingNumber() {
        return functionCuttingNumber;
    }

    public void setFunctionCuttingNumber(double functionCuttingNumber) {
        this.functionCuttingNumber = functionCuttingNumber;
    }



    public double getDependencies() {
        return dependencies;
    }

    public void setDependencies(double dependencies) {
        this.dependencies = dependencies;
    }

    public double getWeightedDependencyCost() {
        return weightedDependencyCost;
    }

    public void setWeightedDependencyCost(double weightedDependencyCost) {
        this.weightedDependencyCost = weightedDependencyCost;
    }

    public double getWeightedDependencyCostSQ() {
        return weightedDependencyCostSQ;
    }

    public void setWeightedDependencyCostSQ(double weightedDependencyCostSQ) {
        this.weightedDependencyCostSQ = weightedDependencyCostSQ;
    }

    public double getNormalizedCycleEdges() {
        return normalizedCyleEdges;
    }

    public void setNormalizedCyleEdges(double normalizedCyleEdges) {
        this.normalizedCyleEdges = normalizedCyleEdges;
    }

    public double getNormalizedCyclePaths() {
        return normalizedCyclePaths;
    }

    public void setNormalizedCyclePaths(double normalizedCyclePaths) {
        this.normalizedCyclePaths = normalizedCyclePaths;
    }


    public double getIncomingDepeConc() {
        return incomingDepeConc;
    }

    public void setIncomingDepeConc(double incomingDepeConc) {
        this.incomingDepeConc = incomingDepeConc;
    }

    public double getOutgoingDepConc() {
        return outgoingDepConc;
    }

    public void setOutgoingDepConc(double outgoingDepConc) {
        this.outgoingDepConc = outgoingDepConc;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public double getComplexity() {
        return complexity;
    }

    public void setComplexity(double complexity) {
        this.complexity = complexity;
    }

    public int getNumOfFunctions() {
        return numOfFunctions;
    }

    public void setNumOfFunctions(int numOfFunctions) {
        this.numOfFunctions = numOfFunctions;
    }

    public int getNumOfFiles() {
        return numOfFiles;
    }

    public void setNumOfFiles(int numOfFiles) {
        this.numOfFiles = numOfFiles;
    }

    public double getAvgFileSize() {
        return avgFileSize;
    }

    public void setAvgFileSize(double avgFileSize) {
        this.avgFileSize = avgFileSize;
    }

    public int getFileMaxSize() {
        return fileMaxSize;
    }

    public void setFileMaxSize(int fileMaxSize) {
        this.fileMaxSize = fileMaxSize;
    }

    public double getFileSizeDeviation() {
        return fileSizeDeviation;
    }

    public void setFileSizeDeviation(double fileSizeDeviation) {
        this.fileSizeDeviation = fileSizeDeviation;
    }

}
