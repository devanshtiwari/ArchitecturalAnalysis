package metrics;

import dependencyManager.Cluster;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.BiconnectivityInspector;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;
import org.jgrapht.io.ExportException;

import java.util.*;

import static metrics.Metrics.inoutDegree;

public class MetricsContainer {

    private String projectName;
    private MetricsInputContent metricsInputContent;

    private int numOfFiles = -1;
    private int numOfFunctions = -1;
    private int fileMaxSize = -1;
    private double avgFileSize = -1;
    private double fileSizeDeviation = -1;
    private int numOfModules = -1;//

    private int numOfFilesGraph = -1;
    private int numOfFunctionsGraph = -1;
    private int fileMaxSizeGraph = -1;
    private double avgFileSizeGraph = -1;
    private double fileSizeDeviationGraph = -1;
    private int numOfModulesGraph = -1;//

    private double dependencyConc = -1;
    private double avgModuleDensity = -1;
    private double avgParameterSize = -1;
    private double avgParameterSizeGraph = -1;//

    private double moduleCallComplexity = -1;
    private double fileCallComplexity = -1;
    private double functionCallComplexity = -1;

    private double moduleDependencies = -1;
    private double fileDependencies = -1;
    private double functionDependencies = -1;

    private double moduleDensity = -1;
    private double fileDensity = -1;
    private double functionDensity = -1;

    private double moduleCuttingNumber = -1;
    private double fileCuttingNumber = -1;
    private double functionCuttingNumber = -1;

    private double qValue = -1;

    private double avgInstability = -1;

    private double modulePropagationCost = -1;
    private double filePropagationCost = -1;
    private double functionPropagationCost = -1;

    private double moduleCycles = -1;
    private double fileCycles = -1;
    private double functionCycles = -1;

    private double moduleConnectedSets = -1;
    private double fileConnectedSets = -1;
    private double functionConnectedSets = -1;

    private double moduleCyclesMin = -1;
    private double fileCyclesMin = -1;
    private double functionCyclesMin = -1;

    public List<String> getRow(){
        return Arrays.asList(projectName, Integer.toString(numOfFiles),Integer.toString(numOfFunctions), Integer.toString(fileMaxSize),
                Double.toString(avgFileSize), Double.toString(fileSizeDeviation), Integer.toString(numOfModules),Integer.toString(numOfFilesGraph),
                Integer.toString(numOfFunctionsGraph), Integer.toString(fileMaxSizeGraph), Double.toString(avgFileSizeGraph),
                Double.toString(fileSizeDeviationGraph), Integer.toString(numOfModulesGraph),
                Double.toString(dependencyConc), Double.toString(avgModuleDensity),
                Double.toString(avgParameterSize), Double.toString(avgParameterSizeGraph), Double.toString(moduleCallComplexity), Double.toString(fileCallComplexity),
                Double.toString(functionCallComplexity), Double.toString(moduleDependencies), Double.toString(fileDependencies),
                Double.toString(functionDependencies), Double.toString(moduleDensity), Double.toString(fileDensity), Double.toString(functionDensity),
                Double.toString(moduleCuttingNumber), Double.toString(fileCuttingNumber), Double.toString(functionCuttingNumber),
                Double.toString(qValue), Double.toString(avgInstability), Double.toString(modulePropagationCost), Double.toString(filePropagationCost),
                Double.toString(functionPropagationCost), Double.toString(moduleCycles), Double.toString(fileCycles), Double.toString(functionCycles),
                Double.toString(moduleConnectedSets), Double.toString(fileConnectedSets), Double.toString(functionConnectedSets),
                Double.toString(moduleCyclesMin), Double.toString(fileCyclesMin), Double.toString(functionCyclesMin));
    }

    public static List<String> getHeader(){
        return Arrays.asList("ProjectName", "Number of Files", "Number of Functions", "File Maximum Size",
                "Average File Size", "File Size Deviation", "Number of Modules", "Number of Files(Graph)",
                "Number of Functions(Graph)", "File Maximum Size(Graph)", "Average File Size(Graph)",
                "File Size Deviation Graph", "Number of Modules (Graph)",
                "Dependency Concentration", "Average Module Density",
                "Average Parameter Size", "Average Parameter Size (Graph)", "Module Call Complexity", "File Call Compelexity",
                "Function Call Complexity", "Module Dependencies", "File Dependencies",
                "Function Dependencies", "Module Density", "File Density", "Function Density",
                "Module Cutting Number", "File Cutting Number", "Function Cutting Number",
                "Q Value", "Average Instability", "Module Propagation Cost", "File Propagation Cost",
                "Function propagation Cost", "Module Cycles", "File Cycles", "Function Cycles",
                "Module Connected Sets", "File Connected Sets", "Function Connected Sets",
                "Modules Cycles Minimum", "File Cycles Minimum", "Function Cycles Minimum");
    }

    public List<List<String>> getModuleNodeMetricsCSV(){
        List<List<String>> csv = new ArrayList<>();
        csv.add(ModuleBasedMetric.getHeader());
        for(String module : moduleBasedMetrics.keySet()){
            csv.add(moduleBasedMetrics.get(module).getRow());
        }
        return csv;
    }

    public List<List<String>> getFileNodeMetricsCSV(){
        List<List<String>> csv = new ArrayList<>();
        csv.add(FileBasedMetric.getHeader());
        for(String file : fileBasedMetrics.keySet()){
            csv.add(fileBasedMetrics.get(file).getRow());
        }
        return csv;
    }

    public List<List<String>> getFunctionNodeMetricsCSV(){
        List<List<String>> csv = new ArrayList<>();
        csv.add(FunctionBasedMetric.getHeader());
        for(String function : functionBasedMetrics.keySet()){
            csv.add(functionBasedMetrics.get(function).getRow());
        }
        return csv;
    }




    private HashMap<String, ModuleBasedMetric> moduleBasedMetrics = new HashMap<>();
    private HashMap<String, FileBasedMetric> fileBasedMetrics = new HashMap<>();
    private HashMap<String, FunctionBasedMetric> functionBasedMetrics = new HashMap<>();

    public MetricsContainer(MetricsInputContent metricsInputContent) {
        this.projectName = metricsInputContent.getProjectName();
        this.metricsInputContent = metricsInputContent;
    }


    public void calculateStats() throws ExportException {

        initializeSubs();

        projectBasicStats();

        sizeDeviation();

        dependencyConc();

        callComplexity();

        dependencies();

        inOutDegrees();

        density();

        cuttingNumber();

        avgModuleDensity();

        QValue();

        instability();

        fanInfantOutVisibility();

        abstraction();

        distance();

        externalFunctionUsage();

        dependentModules();

        cycles();

    }

    private void initializeSubs(){
        for(String module : metricsInputContent.getModuletoFileMap().keySet()){
            ModuleBasedMetric moduleBasedMetric = new ModuleBasedMetric();
            moduleBasedMetric.moduleName = module;
            moduleBasedMetrics.put(module, moduleBasedMetric);
        }
        for(String file : metricsInputContent.getFiletoFunctionMap().keySet()){
            FileBasedMetric fileBasedMetric = new FileBasedMetric();
            fileBasedMetric.fileName = file;
            fileBasedMetrics.put(file,fileBasedMetric);
            for(String function : metricsInputContent.getFiletoFunctionMap().get(file)){
                FunctionBasedMetric functionBasedMetric = new FunctionBasedMetric();
                functionBasedMetric.fileName = file;
                functionBasedMetric.onlyFunctionName = function;
                functionBasedMetric.functionName = file + "/" + function;
                functionBasedMetrics.put(functionBasedMetric.functionName, functionBasedMetric);
            }
        }
    }


    private void projectBasicStats() {
        int maxFunctions = 0;;

        int functionSum = 0, parameterSum = 0;
        int parameterSumGraph = 0;

        for(String module : metricsInputContent.getModuletoFileMap().keySet()){
            HashSet<String> files = metricsInputContent.getModuletoFileMap().get(module);
            double pSumInModule = 0;
            double pSumInModuleGraph = 0;
            int moduleWiseFunctions = 0, moduleFileMaxSize = 0;
            int moduleWiseFunctionsGraph = 0, moduleFileMaxSizeGraph = 0;
            int fileSizeGraph = 0;
            for(String file : files){
                int numOfFunctionsGraph = 0;
                HashSet<String> functions = metricsInputContent.getFiletoFunctionMap().get(file);
                fileBasedMetrics.get(file).numOfFunctions = functions.size();
                moduleWiseFunctions += fileBasedMetrics.get(file).numOfFunctions;
                int pSumOfFunctionsinFile = 0, pSumOfFunctionsinFileGraph = 0;
                if(moduleFileMaxSize < fileBasedMetrics.get(file).numOfFunctions)
                    moduleFileMaxSize = fileBasedMetrics.get(file).numOfFunctions;

                for(String function : functions){
                    if(metricsInputContent.getGraph().getFunctionGraph().vertexSet().contains(file + "/" + function)) {
                        fileSizeGraph++;
                        numOfFunctionsGraph++;
                        functionBasedMetrics.get(file + "/" + function).numOfParametersGraph = metricsInputContent.getFunctionCSVInfoArrayList().get(file + "/" + function).getCount();
                        pSumOfFunctionsinFileGraph += functionBasedMetrics.get(file + "/" + function).numOfParametersGraph;
                    }
                    functionBasedMetrics.get(file + "/" + function).numOfParameters = metricsInputContent.getFunctionCSVInfoArrayList().get(file + "/" + function).getCount();
                    pSumOfFunctionsinFile += functionBasedMetrics.get(file + "/" + function).numOfParameters;
                }
                if(metricsInputContent.getGraph().getFileGraph().vertexSet().contains(file)) {
                    fileBasedMetrics.get(file).numOfFunctionsGraph = numOfFunctionsGraph;
                    moduleWiseFunctionsGraph += fileBasedMetrics.get(file).numOfFunctionsGraph;
                    if(moduleFileMaxSizeGraph < fileBasedMetrics.get(file).numOfFunctionsGraph)
                        moduleFileMaxSizeGraph = fileBasedMetrics.get(file).numOfFunctionsGraph;
                    if(fileBasedMetrics.get(file).numOfFunctionsGraph == 0){
                        fileBasedMetrics.get(file).avgParametersGraph = -1;
                    }
                    else
                    fileBasedMetrics.get(file).avgParametersGraph = pSumOfFunctionsinFileGraph/fileBasedMetrics.get(file).numOfFunctionsGraph;
                    pSumInModuleGraph += pSumOfFunctionsinFileGraph;
                    parameterSumGraph += pSumOfFunctionsinFileGraph;
                }

                fileBasedMetrics.get(file).avgParameters = pSumOfFunctionsinFile/functions.size();
                pSumInModule += pSumOfFunctionsinFile;
                parameterSum += pSumOfFunctionsinFile;
            }
            if(maxFunctions < moduleFileMaxSize)
                maxFunctions = moduleFileMaxSize;
            functionSum += moduleWiseFunctions;
            moduleBasedMetrics.get(module).fileMaxSize = moduleFileMaxSize;
            moduleBasedMetrics.get(module).moduleName = module;
            moduleBasedMetrics.get(module).numOfFiles = files.size();
            moduleBasedMetrics.get(module).avgFileSize = moduleWiseFunctions/files.size();
            moduleBasedMetrics.get(module).numOfFunctions = moduleWiseFunctions;
            moduleBasedMetrics.get(module).avgParameters = pSumInModule/moduleWiseFunctions;
            if(metricsInputContent.getGraph().getDirectoryGraph().vertexSet().contains(module)){
                moduleBasedMetrics.get(module).fileMaxSizeGraph = moduleFileMaxSizeGraph;
                moduleBasedMetrics.get(module).numOfFilesGraph = fileSizeGraph;
                moduleBasedMetrics.get(module).avgFileSizeGraph = (double) moduleWiseFunctionsGraph/ (double) fileSizeGraph;
                moduleBasedMetrics.get(module).numOfFunctionsGraph = moduleWiseFunctionsGraph;
                moduleBasedMetrics.get(module).avgParametersGraph = pSumInModuleGraph/moduleWiseFunctionsGraph;
            }
        }

        this.numOfFunctions = functionSum;
        this.numOfFunctionsGraph = metricsInputContent.getGraph().getFunctionGraph().vertexSet().size();
        this.numOfFiles = metricsInputContent.getFiletoFunctionMap().size();
        this.numOfFilesGraph =metricsInputContent.getGraph().getFileGraph().vertexSet().size();

        this.avgFileSize = functionSum/this.numOfFiles;
        this.avgFileSizeGraph = (double) this.numOfFunctionsGraph/(double) this.numOfFilesGraph;

        int max = 0;
        for (String key : metricsInputContent.getGraph().getFunctionsInFile().keySet()) {
            if (max < metricsInputContent.getGraph().getFunctionsInFile().get(key))
                max = metricsInputContent.getGraph().getFunctionsInFile().get(key);
        }
        this.fileMaxSizeGraph = max;
        this.fileMaxSize = maxFunctions;
        this.avgParameterSize = parameterSum/this.numOfFunctions;

        this.avgParameterSizeGraph = parameterSumGraph/this.numOfFunctionsGraph;
        this.numOfModulesGraph = metricsInputContent.getGraph().getDirectoryGraph().vertexSet().size();
        this.numOfModules = metricsInputContent.getModuletoFileMap().size();
    }


    private void sizeDeviation() {
        double deviation = 0;
        double mu = this.avgFileSize, D = 0;
        double muGraph = this.avgFileSizeGraph, Dgraph  = 0;

        for(String module : metricsInputContent.getModuletoFileMap().keySet()){
            HashSet<String> files = metricsInputContent.getModuletoFileMap().get(module);
            double moduleDeviation = 0;
            double modulemMu = moduleBasedMetrics.get(module).avgFileSize, moduleD = 0;
            double moduleMuGraph = moduleBasedMetrics.get(module).avgFileSizeGraph, moduleDgraph = 0;
            for(String file: files){
                if(metricsInputContent.getGraph().getFileGraph().vertexSet().contains(file)) {
                    moduleDgraph += Math.abs(fileBasedMetrics.get(file).numOfFunctionsGraph - moduleMuGraph);
                    Dgraph += Math.abs(fileBasedMetrics.get(file).numOfFunctionsGraph - this.avgFileSizeGraph);
                }
                moduleD += Math.abs(fileBasedMetrics.get(file).numOfFunctions - modulemMu);
                D += Math.abs(fileBasedMetrics.get(file).numOfFunctions - this.avgFileSize);
            }
            moduleDeviation = 1.0 - moduleD / (modulemMu * moduleBasedMetrics.get(module).numOfFunctions);
            moduleBasedMetrics.get(module).fileSizeDeviation = moduleDeviation;
            if(metricsInputContent.getGraph().getDirectoryGraph().vertexSet().contains(module)){
                moduleBasedMetrics.get(module).fileSizeDeviationGraph = 1.0 - moduleDgraph / (moduleMuGraph * moduleBasedMetrics.get(module).numOfFunctionsGraph);
            }
        }

        System.out.println(D);
        deviation = 1.0 - D / (mu * this.numOfFunctions);
        System.out.println("File Size Deviation: " + deviation);
        this.fileSizeDeviation = deviation;
        this.fileSizeDeviationGraph = 1.0 - Dgraph / (muGraph * this.numOfFunctionsGraph);
    }

    private void dependencyConc() {
        double dep = 0.0;
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> fileG = metricsInputContent.getGraph().getFileGraph();
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> funcG = metricsInputContent.getGraph().getFunctionGraph();
        double filesumInOut = 0.0, funcsumInOut = 0.0;

        for (String v : fileG.vertexSet()) {
            double[] inout = inoutDegree(fileG, v);
            filesumInOut += inout[0] * inout[0];
        }
        for (String v : funcG.vertexSet()) {
            double[] inout = inoutDegree(funcG, v);
            funcsumInOut += inout[0] * inout[0];
        }
        dep = 1.0 - (numOfFiles * filesumInOut) / (numOfFunctions * funcsumInOut);

        System.out.println("Incoming concentration: " + dep);
        this.dependencyConc = dep;
    }

    private void callComplexity(){

        double numeratorfile = 0, numeratorfunc = 0, numeratormodule = 0;
        int numberOfFiles = 0, numberOfFunctions = 0, numberOfModules = 0;
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> fileG = metricsInputContent.getGraph().getFileGraph();
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> funcG = metricsInputContent.getGraph().getFunctionGraph();
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> moduleG = metricsInputContent.getGraph().getDirectoryGraph();

        for (String file : fileG.vertexSet()) {
            double[] inout = inoutDegree(fileG, file);
            numeratorfile += inout[0] * inout[1];
            numberOfFiles++;
        }

        for (String function : funcG.vertexSet()) {
            double[] inout = inoutDegree(funcG, function);
            numeratorfunc += inout[0] * inout[1];
            numberOfFunctions++;
        }

        for (String module : moduleG.vertexSet()) {
            double[] inout = inoutDegree(moduleG, module);
            numeratormodule += inout[0] * inout[1];
            numberOfModules++;
        }

        this.fileCallComplexity = numeratorfile / ((numberOfFiles - 1) * (numberOfFiles - 1));
        this.functionCallComplexity = numeratorfunc / ((numberOfFunctions - 1) * (numberOfFunctions - 1));
        this.moduleCallComplexity = numeratormodule / ((numberOfModules - 1) * (numberOfModules - 1));

        System.out.println("Call Complexity: " );
    }

    void inOutDegrees(){

        DirectedWeightedPseudograph<String, DefaultWeightedEdge> fileG = metricsInputContent.getGraph().getFileGraph();
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> funcG = metricsInputContent.getGraph().getFunctionGraph();
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> moduleG = metricsInputContent.getGraph().getDirectoryGraph();

        for (String file : fileG.vertexSet()) {
            double[] inout = inoutDegree(fileG, file);
            if(fileBasedMetrics.get(file) != null){
                fileBasedMetrics.get(file).inDegree = inout[0];
                fileBasedMetrics.get(file).outDegree = inout[1];
            }
        }


        for (String function : funcG.vertexSet()) {
            double[] inout = inoutDegree(funcG, function);
            if(functionBasedMetrics.get(function) != null){
                functionBasedMetrics.get(function).inDegree = inout[0];
                functionBasedMetrics.get(function).outDegree = inout[1];
            }
        }

        for (String module : moduleG.vertexSet()) {
            double[] inout = inoutDegree(moduleG, module);
            if(moduleBasedMetrics.get(module) != null){
                moduleBasedMetrics.get(module).inDegree = inout[0];
                moduleBasedMetrics.get(module).outDegree = inout[1];
            }
        }
    }

    void dependencies(){
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> fileG = metricsInputContent.getGraph().getFileGraph();
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> funcG = metricsInputContent.getGraph().getFunctionGraph();
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> moduleG = metricsInputContent.getGraph().getDirectoryGraph();

        double w = 0;
        for (DefaultWeightedEdge e : fileG.edgeSet()) {
            w += fileG.getEdgeWeight(e);
        }
        this.fileDependencies = w / (fileG.vertexSet().size() * fileG.vertexSet().size());

        w = 0;
        for (DefaultWeightedEdge e : funcG.edgeSet()) {
            w += funcG.getEdgeWeight(e);
        }
        this.functionDependencies = w / (funcG.vertexSet().size() * funcG.vertexSet().size());

        w = 0;
        for (DefaultWeightedEdge e : moduleG.edgeSet()) {
            w += moduleG.getEdgeWeight(e);
        }
        this.moduleDependencies = w / (moduleG.vertexSet().size() * moduleG.vertexSet().size());
    }

    void density(){
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> fileG = metricsInputContent.getGraph().getFileGraph();
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> funcG = metricsInputContent.getGraph().getFunctionGraph();
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> moduleG = metricsInputContent.getGraph().getDirectoryGraph();

        this.moduleDensity = (double)moduleG.edgeSet().size()/(double)(moduleG.vertexSet().size()*(moduleG.vertexSet().size()-1));
        this.fileDensity = (double)fileG.edgeSet().size()/(double)(fileG.vertexSet().size()*(fileG.vertexSet().size()-1));
        this.functionDensity = (double)funcG.edgeSet().size()/(double)(funcG.vertexSet().size()*(funcG.vertexSet().size()-1));
    }

    void cuttingNumber(){
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> fileG = metricsInputContent.getGraph().getFileGraph();
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> funcG = metricsInputContent.getGraph().getFunctionGraph();
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> moduleG = metricsInputContent.getGraph().getDirectoryGraph();

        double fileCutting = Metrics.cuttingNumber(fileG);
        if(fileCutting!=-1)
            this.fileCuttingNumber = fileCutting;
        else
            this.fileCuttingNumber = 0;
        double functionCutting = Metrics.cuttingNumber(funcG);
        if(functionCutting!=-1)
            this.functionCuttingNumber = functionCutting;
        else
            this.functionCuttingNumber = 0;
        double moduleCutting = Metrics.cuttingNumber(moduleG);
        if(moduleCutting!=-1)
            this.moduleCuttingNumber = moduleCutting;
        else
            this.moduleCuttingNumber = 0;
    }

    void QValue(){
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> moduleG = metricsInputContent.getGraph().getDirectoryGraph();
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> fileG = metricsInputContent.getGraph().getFileGraph();

        double Qval = 0;
        double n = 0;
        for(DefaultWeightedEdge e : fileG.edgeSet()){
            n += 2 * moduleG.getEdgeWeight(e);
        }
        for(String module : moduleG.vertexSet()){
            double ai = 0, eii=0;
            if(moduleG.getEdge(module,module) !=null)
                eii = 2* moduleG.getEdgeWeight(moduleG.getEdge(module,module));
            ai = moduleBasedMetrics.get(module).outDegree + (eii/2);
            Qval += (eii/n - (ai/n)*(ai/n));
        }
        this.qValue = Qval;
    }

    void avgModuleDensity(){
        HashMap<String, Cluster> clusterMap = metricsInputContent.getGraph().getClusterHashMap(); //Based on graph
        HashMap<String, HashSet<String>> map = metricsInputContent.getModuletoFileMap(); //Based on structure,

        double avg = 0;
        double denominator = 0;
        for(String str:clusterMap.keySet()){
            double dens = clusterMap.get(str).getEdges()/((clusterMap.get(str).getNodes()+1.0)*(clusterMap.get(str).getNodes()));
            avg += dens * clusterMap.get(str).getNodes();
            denominator += clusterMap.get(str).getNodes();
        }

        avg /=  denominator;
        this.avgModuleDensity = avg;
    }

    void instability(){
        double sumInstability = 0D;
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> moduleG = metricsInputContent.getGraph().getDirectoryGraph();

        //TODO Ca.size is module size -> no, Ca.size is based on graph, whereas module is based on structure..
        for(String module : metricsInputContent.getGraph().Ce.keySet()){
            double hold = sumInstability;
            if(!(metricsInputContent.getGraph().Ce.get(module) + metricsInputContent.getGraph().Ce.get(module) == 0))
                sumInstability += metricsInputContent.getGraph().Ce.get(module)/(metricsInputContent.getGraph().Ca.get(module) + metricsInputContent.getGraph().Ce.get(module));
            moduleBasedMetrics.get(module).instability = sumInstability - hold;
        }
        this.avgInstability = sumInstability / metricsInputContent.getGraph().Ca.size();
    }

    void fanInfantOutVisibility() throws ExportException {
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> fileG = metricsInputContent.getGraph().getFileGraph();
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> funcG = metricsInputContent.getGraph().getFunctionGraph();
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> moduleG = metricsInputContent.getGraph().getDirectoryGraph();


//        LinkedHashMap<String, double[]> fileLinkedHashMapMatrix = Metrics.matrix(fileG);
//        LinkedHashMap<String, double[]> linkedHashMapMatrix = Metrics.matrix(g);



        HashMap<String, double[]> fileFinalMatrix = getFinalMatrix(fileG);
//        HashMap<String, double[]> funcFinalMatrix = getFinalMatrix(funcG);
        HashMap<String, double[]> moduleFinalMatrix = getFinalMatrix(moduleG);

        double propagationCost = 0;
        propagationCost = calculateFanInFanOut(fileG, fileBasedMetrics, fileFinalMatrix);
        this.filePropagationCost = propagationCost;
        propagationCost = 0;
//      propagationSum = calculateFanInFanOut(funcG, functionBasedMetrics, funcFinalMatrix);
        this.functionPropagationCost = propagationCost;
        propagationCost = 0;
        if(moduleFinalMatrix != null) {
            propagationCost = calculateFanInFanOut(moduleG, moduleBasedMetrics, moduleFinalMatrix);
            this.modulePropagationCost = propagationCost;
        }

//        double adMatrix[][] = new double[fileLinkedHashMapMatrix.size()][fileLinkedHashMapMatrix.size()];
//        int l = 0;
//        for(String s : fileLinkedHashMapMatrix.keySet()){
//            double []t = fileLinkedHashMapMatrix.get(s);
//            adMatrix[l++] = t;
//        }

//        RealMatrix realMatrix = MatrixUtils.createRealMatrix(adMatrix);
//        RealMatrix finalMatrix = MatrixUtils.createRealIdentityMatrix(realMatrix.getColumnDimension());
//
//
//        for(int i = 1; i<realMatrix.getColumnDimension()-1; i++) {
//            finalMatrix = finalMatrix.add(realMatrix.power(i));
//        }
//        System.out.println(finalMatrix);


//        double sum = 0;
//        double[][] f = finalMatrix.getData();
//
//        for(int i = 0; i< f.length;i++){
//            for(int j=0; j<f.length;j++){
//                if(f[i][j]!=0)
//                    sum += 1;
//            }
//        }
//        if(f.length == 1)
//            System.out.println("The value of Propagation Cost is: " + 0);
//        else
//            System.out.println("The value of Propagation Cost is: " + sum/(f.length*f.length));
    }

    double calculateFanInFanOut(DirectedWeightedPseudograph<String, DefaultWeightedEdge> g, HashMap hashMap, HashMap<String, double[]> linkedHashMapMatrix) throws ExportException {
//        LinkedHashMap<String, double[]> fileLinkedHashMapMatrix = Metrics.matrix(fileG);
//        LinkedHashMap<String, double[]> functionLinkedHashMapMatrix = Metrics.matrix(funcG);
//        LinkedHashMap<String, double[]> moduleLinkedHashMapMatrix = Metrics.matrix(moduleG);


        assert linkedHashMapMatrix != null;

        double [] columnWise = new double[linkedHashMapMatrix.size()];
        int k = 0;
        int sum1 = 0, sum2 = 0;
        for(String file : linkedHashMapMatrix.keySet()){
            if(hashMap.get(file)==null)
                continue;
            //Condition that if the file does not have recongizable function, then ignore it.
            double [] row = linkedHashMapMatrix.get(file);
            double rowWise = 0;
            for(int i = 0; i < row.length; i++){
                rowWise += row[i];
                columnWise[i] += row[i];
            }

            ((MetricsClass)hashMap.get(file)).fanInVisibility = rowWise / row.length;
            sum1 += rowWise;
        }
        k = 0;
        for(String file : linkedHashMapMatrix.keySet()){
            if(hashMap.get(file) != null) {
                ((MetricsClass)hashMap.get(file)).fanOutVisibility = columnWise[k]/columnWise.length;
            }
            sum2 += columnWise[k];

            k++;
        }

        return  ((double)sum1/(double)columnWise.length);
    }

    HashMap<String, double[]> getFinalMatrix(DirectedWeightedPseudograph<String, DefaultWeightedEdge> g) throws ExportException {
        LinkedHashMap<String, double[]> fileLinkedHashMapMatrix = Metrics.matrix(g);
        if(fileLinkedHashMapMatrix == null)
            return null;
        double adMatrix[][] = new double[fileLinkedHashMapMatrix.size()][fileLinkedHashMapMatrix.size()];
        int l = 0;
        for(String s : fileLinkedHashMapMatrix.keySet()){
            double []t = fileLinkedHashMapMatrix.get(s);
            adMatrix[l++] = t;
        }
        RealMatrix realMatrix = MatrixUtils.createRealMatrix(adMatrix);
        RealMatrix finalMatrix = MatrixUtils.createRealIdentityMatrix(realMatrix.getColumnDimension());

        for(int i = 1; i<realMatrix.getColumnDimension()-1; i++) {
            finalMatrix = finalMatrix.add(realMatrix.power(i));
        }
        int i = 0;
        HashMap<String, double[]> finalFileLinkedHashMapMatrix = new HashMap<>();
        for(String s : fileLinkedHashMapMatrix.keySet()){
            finalFileLinkedHashMapMatrix.put(s,finalMatrix.getRow(i++));
        }
        return finalFileLinkedHashMapMatrix;
    }

    void abstraction(){
        //Header FIles divided by the number of files in the module
        double cFiles = 0, hFiles = 0;
         for(String module : metricsInputContent.getModuletoFileMap().keySet()){
             HashSet<String> files = metricsInputContent.getModuletoFileMap().get(module);
             for(String file : files){
                 if(file.endsWith(".c"))
                     cFiles++;
                 else if(file.endsWith(".h"))
                     hFiles++;
             }
             moduleBasedMetrics.get(module).abstraction = hFiles/cFiles; //TODO THis can be improved. header files only called by files inside.
         }
    }

    void distance(){
        for(String module : metricsInputContent.getModuletoFileMap().keySet()){
            if(moduleBasedMetrics.get(module) != null)
                moduleBasedMetrics.get(module).distance = Math.abs(moduleBasedMetrics.get(module).abstraction + moduleBasedMetrics.get(module).instability -1);
        }
    }

    void externalFunctionUsage(){
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> funcG = metricsInputContent.getGraph().getFileGraph();
        for(String module : metricsInputContent.getModuletoFileMap().keySet()){
            HashSet<String> files = metricsInputContent.getModuletoFileMap().get(module);
            for(String file : files){
                if(fileBasedMetrics.get(file) != null && funcG.containsVertex(file))
                    fileBasedMetrics.get(file).externalFunctionsCalled = funcG.outDegreeOf(file);
            }
        }
    }

    void dependentModules(){
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> moduleG = metricsInputContent.getGraph().getDirectoryGraph();
        for(String module : metricsInputContent.getModuletoFileMap().keySet()){
            if(moduleBasedMetrics.get(module) != null && moduleG.containsVertex(module))
                moduleBasedMetrics.get(module).dependentModules = moduleG.outDegreeOf(module); //TODO CONFIRM!!!!!!!!!
        }
    }

    void cycles(){
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> fileG = metricsInputContent.getGraph().getFileGraph();
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> funcG = metricsInputContent.getGraph().getFunctionGraph();
        DirectedWeightedPseudograph<String, DefaultWeightedEdge> moduleG = metricsInputContent.getGraph().getDirectoryGraph();

        if(cyclesinGraph(fileG, metricsInputContent.getGraph().mainFile)!=-1) {
            this.fileCycles = cycleAllEdges.size();
            this.fileCyclesMin = cycleMinEdges.size();
        }
        this.fileConnectedSets = connectedSets(fileG);
        cyclesinGraph(funcG, metricsInputContent.getGraph().mainFunc);
        this.functionCycles = cycleAllEdges.size();
        this.functionCyclesMin = cycleMinEdges.size();
        this.functionConnectedSets = connectedSets(funcG);
        cyclesinGraph(moduleG, metricsInputContent.getGraph().mainModule);
        this.moduleCycles = cycleAllEdges.size();
        this.moduleCyclesMin = cycleMinEdges.size();
        this.moduleConnectedSets = connectedSets(moduleG);
    }

    int cyclesinGraph(DirectedWeightedPseudograph<String, DefaultWeightedEdge> g, String mainV){


        globalPathFindergraph = null; cycleAllEdges.clear(); cycleMinEdges.clear();
        if(!g.containsVertex(mainV))
            return -1;
        ArrayList pathSoFar = new ArrayList<String>();
        if(mainV.isEmpty()) {
            mainV = g.vertexSet().iterator().next();
            System.out.println("wow");
        }
        pathSoFar.add(mainV);
        globalPathFindergraph = (DirectedWeightedPseudograph<String, DefaultWeightedEdge>) g.clone();
        findPathWithCycles(mainV, pathSoFar);
        return 1;
    }

    DirectedWeightedPseudograph<String, DefaultWeightedEdge> globalPathFindergraph;

    private HashSet<DefaultWeightedEdge> cycleAllEdges = new HashSet<>();
    private HashSet<DefaultWeightedEdge> cycleMinEdges = new HashSet<>();


    private void findPathWithCycles(String mainV, ArrayList pathSoFar) {

        //Some problem with the csv
        if (projectName.equals("ImageMagick"))
            return;
        for (String node : Graphs.successorListOf(globalPathFindergraph, mainV)) {

            if (pathSoFar.contains(node)) {
                Iterator<String> pathSoFarIterator = pathSoFar.iterator();
                String n1 = "";
                if (pathSoFarIterator.hasNext())
                    n1 = pathSoFarIterator.next();
                while (pathSoFarIterator.hasNext()) {
                    if (pathSoFarIterator.hasNext()) {
                        String n2 = pathSoFarIterator.next();
                        cycleAllEdges.add(globalPathFindergraph.getEdge(n1, n2));
                        n1 = n2;
                    }
                }
                cycleAllEdges.add(globalPathFindergraph.getEdge(mainV, node));
                cycleMinEdges.add(globalPathFindergraph.getEdge(mainV, node));
                //Removing edge attempt
                globalPathFindergraph.removeEdge(globalPathFindergraph.getEdge(mainV, node));
            } else {
                ArrayList<String> newPath = new ArrayList<>(pathSoFar);
                newPath.add(node);
                findPathWithCycles(node, newPath);
            }

        }
        pathSoFar.clear();
    }

    int connectedSets(DirectedWeightedPseudograph<String, DefaultWeightedEdge> g) {
        BiconnectivityInspector con = new BiconnectivityInspector(g);
        System.out.println("\nConnected Sets: " + con.getConnectedComponents());
        Iterator itr = con.getConnectedComponents().iterator();
        int countSingletons = 0;
        while (itr.hasNext()) {
            AsSubgraph subGraphs = (AsSubgraph) itr.next();
            if (subGraphs.vertexSet().size() == 1)
                countSingletons++;
        }
        System.out.println("Singletons: " + countSingletons);
        System.out.println("Rest: " + (con.getConnectedComponents().size() - countSingletons));
        return (con.getConnectedComponents().size() - countSingletons);

        //This thing is not that much.
//        KosarajuStrongConnectivityInspector<String, DefaultWeightedEdge> kosarajuStrongConnectivityInspector = new KosarajuStrongConnectivityInspector<>(g);
//
//        List<Set<String>> sets = kosarajuStrongConnectivityInspector.stronglyConnectedSets();
//        for(Set<String> st : sets){
//            if(st.size()>1)
//                System.out.println(st);
//        }
//
//    }
    }

}

