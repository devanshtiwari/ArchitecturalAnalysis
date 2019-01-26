package projectReader;

import fileIO.ProjectDirectoryReader;
import javafx.util.Pair;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandProcessor {

    public HashMap<File, ArrayList<FunctionPoint>> functions = new HashMap<>();
    public HashMap<String, ArrayList<FunctionPoint>> functionsHFNAME = new HashMap<>();
    private HashMap<File, HashSet<String>> fileHeaders = new HashMap<>();
    private  HashSet<FunctionPoint> projectFunctionsList = new HashSet<>();
    long timeTaken = 0;


    HashMap<File, HashSet<File>> headerToFile = new HashMap<>();
    HashMap<String, HashSet<File>> headerToFileName = new HashMap<>();

    public HashMap<File, HashSet<File>> getHeaderToFile() {
        return headerToFile;
    }

    private static final String PATTERN1 = "^\\{\\s+(\\d+)}(\\s+)(\\S+)\\(\\)\\s?(<(unsigned |__const unsigned |__inline |struct |const |__const |__inline__ |)(\\S+)\\s(\\*\\S+\\s|)(\\S+)\\s(\\(([^)]+)\\)\\s)?\\(([^)]+)?\\)\\sat\\s([^:]+):(\\d+)>(\\s\\(R\\))?(\\s\\([^)]+?\\))?)?([:]?)";
    private static final String PATTERN2 = "^\\{\\s+(\\d+)}(\\s+)(\\S+)\\(\\)\\s?(<.*:(\\d+)>)?\\s?(\\(.*\\))?([:]?)?";

    private static final String PATTERN3 = "\\((.*?)\\)";
    public void setAllFunctions(ProjectDirectoryReader projectDirectoryReader){
        try {
            for (File file: projectDirectoryReader.getSourceFiles()) {
                BufferedReader reader = Commands.ctagsFunctionList(file);
                String line = "";
                while ((line = reader.readLine()) != null) {
                    FunctionPoint functionPoint = new FunctionPoint();

                    String[] splits = line.split("\\s+");
                    System.out.println(splits[0] + " " + splits[1]);
                    if (!(splits[1].equals("function") || splits[1].equals("macro") || splits[1].equals("prototype")))
                        continue;
                    functionPoint.setLocation(file);
                    functionPoint.setType(splits[1]);
                    functionPoint.setName(splits[0]);

                    List<String> params = getParameters(line);
                    if(functionPoint.getType().equals("function") && params!=null &&!params.isEmpty()) {
                        if (!params.get(0).equals("void"))
                            functionPoint.addParameter((ArrayList<String>) params);
                    }

                    functions.putIfAbsent(file, new ArrayList<>(Arrays.asList(functionPoint)));
                    functions.computeIfPresent(file,(k,v)->{v.add(functionPoint);return v;});
                    functionsHFNAME.putIfAbsent(file.getName(), new ArrayList<>(Arrays.asList(functionPoint)));
                    functionsHFNAME.computeIfPresent(file.getName(),(k,v)->{v.add(functionPoint);return v;});
                    projectFunctionsList.add(functionPoint);

//                    if(!functions.containsKey(file))
//                        functions.put(file, new ArrayList<FunctionPoint>(Arrays.asList(functionPoint)));
//                    else {
//                        System.out.println("bam");
//                        functions.putif(functions.get(projectDirectoryReader.getProjectFile()).add(functionPoint);
//                        FunctionPoint var = functions.get(splits[0]);
//                        System.out.println("whh");
//                    }
                }
//            proc.waitFor();
            }

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public HashSet<FunctionPoint> getProjectFunctionsList() {
        return projectFunctionsList;
    }

    private ArrayList<String> getParameters(String line){
        ArrayList<String> params = new ArrayList<>();
        Stack<Character> s= new Stack<>();
        int size = line.length() - 1;
        if(line.charAt(size) != ')')
            return params;
        try {
            for (int i = size; i >= 0; i--) {
                if (line.charAt(i) == ')')
                    s.push(line.charAt(i));
                else if (line.charAt(i) == '(') {
                    s.pop();
                    if (s.empty()) {
                        params.addAll(Arrays.asList(line.substring(i + 1, size - 1).split(",")));
                        return params;
                    }
                }
            }
        }catch (StringIndexOutOfBoundsException e){
            return params;
        }

        return params;
    }

    public HashSet<String> getHeadersIncluded(File file){
        HashSet<String> headers = new HashSet<>();
        try {

            BufferedReader reader = Commands.ctagsHeaderInfo(file);
            String line = "";
            while((line = reader.readLine()) != null) {
                String[] splits = line.split("\\s+");
                headers.add(splits[0].substring(splits[0].lastIndexOf("/")+1));
            }
//            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return headers;
    }


    public List<List<String>> setHeaders(ProjectDirectoryReader projectDirectoryReader){

        List<List<String>> fileHeadersCSV = new ArrayList<>();
        List<File> dirHeaders = projectDirectoryReader.getHeaderFiles();

        HashSet<File> dirHeadersForUpdate = new HashSet<>();
        for(File file:projectDirectoryReader.getSourceFiles()){
            if(file.getName().endsWith(".h"))
                continue;
            HashSet<String> hf = this.getHeadersIncluded(file.getAbsoluteFile());
            Iterator<String> iterator = hf.iterator();

            while(iterator.hasNext()){
                String hhf = iterator.next();

                Iterator<File> dirHeaderiterator = dirHeaders.iterator();
                Boolean check = false;
                while(dirHeaderiterator.hasNext()){
                    File dirhf = dirHeaderiterator.next();
                    if(dirhf.getName().equals(hhf)) {
                        System.out.println("bikkurishita");
                        dirHeadersForUpdate.add(new File(dirhf.getAbsolutePath()));
                        headerToFile.putIfAbsent(dirhf,new HashSet<>(Arrays.asList(file)));
                        headerToFile.computeIfPresent(dirhf,(k,v)->{v.add(file);return v;});
                        headerToFileName.putIfAbsent(dirhf.getName(),new HashSet<>(Arrays.asList(file)));
                        headerToFileName.computeIfPresent(dirhf.getName(),(k,v)->{v.add(file);return v;});
                        check = true;
                    }
                }
                if(!check)
                    iterator.remove();
            }
            fileHeaders.put(file,hf);
            fileHeadersCSV.add(new ArrayList<>(Arrays.asList(projectDirectoryReader.getProjectFile().toURI().relativize(file.toURI()).getPath(),String.join(";",hf))));
        }
//        dirHeaders = dirHeadersForUpdate;
        return fileHeadersCSV;
    }


    private Matcher patternIdentifier(String line, String pattern){
       Pattern p = Pattern.compile(pattern);
       return p.matcher(line);
    }

    private Pair<Matcher,Integer> whichMatcher(Matcher m1, Matcher m2){
        if(m1.matches()) {
            System.out.println("Matches with 1st");
            return new Pair<>(m1,1);
        }
        else if(m2.matches()) {
            System.out.println("Matches with 2nd");
            return new Pair<>(m2,2);
        }
        else return new Pair<>(null,0);
    }

    private HashMap<FilePoint, FilePoint> generateDependencies(File file){
        //TODO WHy not PAIR
        HashMap<FilePoint, FilePoint> dependencies;
        dependencies = new HashMap<>();
//        ArrayList<CallStack> callStacks = new ArrayList<>();
//        String[] args = new String[] {"/usr/local/bin/ctags "+ "-x --c-kinds=fp " + file.getAbsolutePath()};
        try {
            BufferedReader reader = Commands.cflowCommand(file);

            String line = "";
//            String callee = "";
            Stack<String> callee = new Stack<>();
            int depth = 0;
            int checkNext = 0;
            callee.push("");
            ArrayList<CFlowLine> cFlowLines = new ArrayList<>();



            while( (line = reader.readLine())!=null) {
                System.out.println("--Loop Starts--");
                //TODO DO NOT IGNORE THE DEPTH LATER ON
                //Group0 Complete String
                //Group1 Depth inside {}
                //Group2 Space after that
                //Group3 Function Name (Can be API, like Macro)
                //Group4 Optional Function Details inside <>
                //Group5 Function Return Type Modifier
                //Group6 Function Return Type
                //Group7 Function Return type modifier like  __attribute__((malloc)), *_GL_ATTRIBUTE_MALLOC
                //Group8 Function Name/Macro Name
                //Group9 Macro Parameters with ( )
                //Group10 Macro Parameters
                //Group11 Function Parameters
                //Group12 File Name
                //Group13 Line Number
                //Group14 (R) Recursive
                //Group15 Recursive See <some_num>
                //Group15 Optional Colon

                FilePoint fp1 = new FilePoint();
                FilePoint fp2 = new FilePoint();


//                Pattern p1 = Pattern.compile("^\\{\\s+(\\d+)}(\\s+)(\\S+)\\(\\)\\s?(<(unsigned |__const unsigned |__inline |struct |const |__const |__inline__ |)(\\S+)\\s(\\*\\S+\\s|)(\\S+)\\s(\\(([^)]+)\\)\\s)?\\(([^)]+)?\\)\\sat\\s([^:]+):(\\d+)>(\\s\\(R\\))?(\\s\\([^)]+?\\))?)?([:]?)");
//                Pattern p2 = Pattern.compile("^\\{\\s+(\\d+)}(\\s+)(\\S+)\\(\\)\\s?(<.*:(\\d+)>)?\\s?(\\(.*\\))?([:]?)?");

//                System.out.println(line);

                Matcher matcher1 = patternIdentifier(line, CommandProcessor.PATTERN1);
                Matcher matcher2 = patternIdentifier(line, CommandProcessor.PATTERN2);

                fp1.setFile(file);

                Pair<Matcher, Integer> pair  = whichMatcher(matcher1,matcher2);
                Matcher matcher = pair.getKey();
                int whichMatcher = pair.getValue();

                if (whichMatcher == 1 || whichMatcher ==2) {
                    CFlowLine cFlowLine = new CFlowLine();
                    cFlowLine.setFile(file);
                    if(whichMatcher == 1 && matcher.group(4)!=null ){
                        cFlowLine.line = matcher.group(0);
                        cFlowLine.depth = Integer.parseInt(matcher.group(1));
                        cFlowLine.function.name = matcher.group(3);
                        cFlowLine.function.returntype = matcher.group(6);
                        cFlowLine.function.parameters = matcher.group(11);
                        cFlowLine.function.funSourceFile = matcher.group(12);
                        cFlowLine.lineno = Integer.parseInt(matcher.group(13));
                    }
                    else
                    {
                        cFlowLine.line = matcher.group(0);
                        cFlowLine.depth = Integer.parseInt(matcher.group(1));
                        cFlowLine.function.name = matcher.group(3);
//                        cFlowLine.lineno = Integer.parseInt(matcher.group(9));
                    }
                    System.out.println("Depth: " + depth +"and current depth: "+ cFlowLine.depth);
                    if(depth> cFlowLine.depth){
                        while(depth> cFlowLine.depth){
                            callee.pop();
                            depth--;
                        }
                    }

//                    if(depth - 1 == cFlowLine.depth) {
//                        callee.pop();
//                        System.out.println("Popping out depth-1 condition true");
//                    }
                    if(checkNext == 1 && depth == cFlowLine.depth) {
                        callee.pop();
                        System.out.println("Popping out checknext is true");
                    }

                    if(cFlowLine.depth == 0) {
//                        System.out.println("Whichmaker = " + whichMatcher);
//                        System.out.println("matcher.group(7) = " + matcher.group(7));
//                        System.out.println("equals = " + matcher.group(7).equals(":"));

                        if(whichMatcher==2 && matcher.group(7)!=null && matcher.group(7).equals(":"))
                            callee.push(cFlowLine.function.name);
                        else if(whichMatcher==1&&matcher.group(16)!=null && matcher.group(16).equals(":")) {
                            callee.push(cFlowLine.function.name);
                        }
                        depth = cFlowLine.depth;
                        continue;
                    }


                    fp1.setFunction(callee.peek());
                    fp2.setFunction(cFlowLine.function.name);
                    if(cFlowLine.function.funSourceFile!=null)
                        fp2.setFile(new File(cFlowLine.function.funSourceFile));
                    else{
                        File f = searchFunction(cFlowLine.file, cFlowLine.function.name);
                        if(f!=null)
                            fp2.setFile(f);
                        else
                        {

                            System.out.println("Did not find defined place for " + cFlowLine.getFunction());
                            fp2.setFile(null);
                        }
                    }

                    if(whichMatcher == 1 && matcher.group(16)!=null)
                        System.out.println("Group 16 " + matcher.group(16));
                    else
                        System.out.println("Group 16 is null");
                    if(whichMatcher == 1&&matcher.group(16)!=null && matcher.group(16).equals(":")) {
                        callee.push(cFlowLine.function.name);
                        checkNext = 1;
                    }
                    else if(whichMatcher==2 && matcher.group(7)!=null && matcher.group(7).equals(":")) {
                        callee.push(cFlowLine.function.name);
                        checkNext =1;
                    }
                    else
                        checkNext = 0;
                    depth = cFlowLine.depth;
                    System.out.println("Stack size: " + callee.size());

                    cFlowLines.add(cFlowLine);
                    dependencies.put(fp1,fp2);

                } else {
                    System.out.println("Do not Match: " + line);
                }
                System.out.println("--Loop Ends--");
            }
            reader.close();
//            proc.waitFor();
//            proc.getInputStream().close();
//            proc.getOutputStream().close();

        } catch (IOException  e) {
            e.printStackTrace();
        }
        return dependencies;
    }

    File searchFunction(File calledFile, String fun){
        long startTime = System.currentTimeMillis();

        if(fileHeaders.containsKey(calledFile))
        for(String header:fileHeaders.get(calledFile)) {
            if (headerToFileName.containsKey(header)) {
                for (File fileInHeader : headerToFileName.get(header)) {
                    if (fun.equals("cs_reg_name") && calledFile.getName().equals("test_arm_regression.c"))
                        System.out.println("stop");
                    if (fun.equals("cs_reg_name") && calledFile.getName().equals("test_arm_regression.c") && fileInHeader.getName().equals("cs.c"))
                        System.out.println("stop");
                    if (functions.containsKey(fileInHeader)) {
                        for (FunctionPoint funp : functions.get(fileInHeader)) {
                            if (funp.getName().equals(fun)) {
                                long endTime = System.currentTimeMillis();
                                timeTaken += (endTime - startTime);
                                return funp.getLocation();
                            }
                        }
                    }
                }
            }
        }

        long endTime = System.currentTimeMillis();
        timeTaken += (endTime - startTime);
        return null;
    }


    public HashMap<FilePoint, FilePoint> getDependencies(File file){
        return generateDependencies(file);
    }

}
