package org.foi.common;

public class JavaCodePartsRemover {
    String croatianChar = "(\\w|[ćžšđčŽĆČĐŠ])";
    String croatianChars = "(\\w|[ćžšđčŽĆČĐŠ])+";
    String newLine = "((\r\n)|(\n))";
    String whiteSpace = "\\s";//"([ ]|\t|" + newLine + ")";
    String spaceExcludeNewLine = "[ \t\\x0B\f]";
    String optionalSpace = whiteSpace + "*";
    String mandatorySpace = whiteSpace + "+";

    String accessModifiers = "(public|private|protected)";
    String optionalAccessModifiers = "(" + accessModifiers + mandatorySpace + ")?";
    String extandedAccessModifiers = "("+accessModifiers+"|(static|abstract|final|synchronized))";
    String allOptionsAccessModifiersOptioanl
            = "("+extandedAccessModifiers+mandatorySpace+"){0,4}";
    String allOptionsAccessModifiersOneMandatroy
            = "("+extandedAccessModifiers+mandatorySpace+"){1,4}";

    String genericType = "("+croatianChar+"|[<>,.\\[\\]?]|"+whiteSpace+")+";
    String simpleType = "((("+croatianChar+"|[.])+)|("+croatianChars+optionalSpace+"\\[\\]))";
    String collectionType = simpleType + optionalSpace + "<" + genericType + ">";
    String type = "(" + simpleType + "|" + collectionType + ")";

    String classFieldUsageMandatoryThis = "(this" + optionalSpace + "." + optionalSpace + croatianChars+")";
    String classFieldUsageOptionalThis = "(("+croatianChars+")|" + classFieldUsageMandatoryThis + ")";

    String inputParameters = "(" + type + mandatorySpace + croatianChars + optionalSpace + "(," + optionalSpace + type + mandatorySpace + croatianChars + optionalSpace + ")*)";
    String passingParameters = "(("+croatianChar+"|\\.)+" + optionalSpace + "(," + optionalSpace + "("+croatianChar+"|\\.)+" + optionalSpace + ")*)";
    String constructorSuperCall = "(super" + optionalSpace + "\\(" + optionalSpace + passingParameters + "?" + optionalSpace + "\\)" + optionalSpace + ";)";

    String importStatement = "import" + mandatorySpace + "(static" + mandatorySpace + ")?("+croatianChars + optionalSpace + "\\." + optionalSpace + ")*("+croatianChars+"|\\*)" + optionalSpace + ";";
    String packageStatement = "package" + mandatorySpace + croatianChars + optionalSpace + "(\\." + optionalSpace + croatianChars + optionalSpace + ")*" + optionalSpace + ";";
    String nonInitializedFields = allOptionsAccessModifiersOneMandatroy + type + mandatorySpace + croatianChars + optionalSpace + ";";

    String optionalVariableTypeWithAccessModifier = allOptionsAccessModifiersOptioanl+"("+type+mandatorySpace+")?"
            +"(this"+optionalSpace+"."+optionalSpace+")?";
    String mandatoryVariableTypeWithAccessModifier = allOptionsAccessModifiersOneMandatroy+"("+type+mandatorySpace+")?"
            +"(this"+optionalSpace+"."+optionalSpace+")?";
    String simpleIntializatorClassVariableStatement =
            allOptionsAccessModifiersOptioanl + type + mandatorySpace + croatianChars + optionalSpace + ";";
            //mandatoryVariableTypeWithAccessModifier+croatianChars+optionalSpace+"(="+optionalSpace
            //        +"(\\("+optionalSpace+type+optionalSpace+"\\)"+optionalSpace+")?"
            //        +"("+croatianChar+"|[\"\'.\\[\\]])+"+optionalSpace+")?"+";";
    //String verySimpleLineRemover =
    //                "(^|"+newLine+")((?!(if)).){100,}[^;\n{}]*;";
    String simpleInitializatorStatemet =
                    optionalVariableTypeWithAccessModifier+"("+croatianChar+"|[.])+"+optionalSpace+"=("+optionalSpace
                   +"(\\("+optionalSpace+type+optionalSpace+"\\)"+optionalSpace+")?"
                  +"("+croatianChar+"|[\"\'.\\[\\]])+"+optionalSpace+")?"+";";
    String simpleObjectInitializatorStatemnt =
            "(^|"+newLine+")"+spaceExcludeNewLine+"*"+optionalVariableTypeWithAccessModifier+"("+croatianChar+"|[.])+"+optionalSpace+"="+optionalSpace
                    +"new"+optionalSpace+"("+croatianChar+"|[<>])+"+optionalSpace+"\\("+optionalSpace+"\\)"+optionalSpace+";";
    String complexObjectInitializatorStatemnt =
            "(^|"+newLine+")"+spaceExcludeNewLine+"*"+optionalVariableTypeWithAccessModifier+"("+croatianChar+"|[.])+"+optionalSpace+"="+optionalSpace
                    +"new"+optionalSpace+"("+croatianChar+"|[<>])+"+optionalSpace+"\\([^{}]*\\)"+optionalSpace+";";

    String functionCallStatement = "(^|"+newLine+")"+spaceExcludeNewLine+"*"+optionalVariableTypeWithAccessModifier+"("+"("+croatianChar+"|[.])+"+optionalSpace+"=)?"+optionalSpace+
            "((\\("+optionalSpace+type+optionalSpace+"\\)"+optionalSpace+")?"+
            "(("+croatianChar+"|[.])+"+optionalSpace+")?"+optionalSpace+
            croatianChars+optionalSpace+"\\([^{};]*\\)"+optionalSpace+");";
    String functionCallStatement2 = "(^|"+newLine+")"+spaceExcludeNewLine+"*"+optionalVariableTypeWithAccessModifier+optionalSpace+
            "((\\("+optionalSpace+type+optionalSpace+"\\)"+optionalSpace+")?"+
            "(("+croatianChar+")+"+optionalSpace+"[.]"+optionalSpace+")*"+optionalSpace+
            croatianChars+optionalSpace+"\\([^{};=]*\\)"+optionalSpace+");";
    String initStatemet = "(^|"+newLine+")"+spaceExcludeNewLine+"*"+optionalVariableTypeWithAccessModifier+"("+croatianChar+"|[.])+"+optionalSpace+"="+optionalSpace
            +"[^;{}]+"+optionalSpace+";";


    String optionalGeneric = "("+optionalSpace+"<"+optionalSpace+croatianChar+optionalSpace+">"+optionalSpace+")?";
    String emptyClass = allOptionsAccessModifiersOptioanl + "class" + mandatorySpace + croatianChars +optionalGeneric
            + "(" + mandatorySpace + "(extends|implements)" + mandatorySpace + type
            + "(" + optionalSpace + "," + optionalSpace + type + ")*)*" + optionalSpace + "\\{" + optionalSpace + "\\}";

    String setMethod = allOptionsAccessModifiersOptioanl + "void" + mandatorySpace + "set"+croatianChars + optionalSpace
            + "\\(" + optionalSpace + type + mandatorySpace + croatianChars + optionalSpace + "\\)" + optionalSpace
            + "\\{" + optionalSpace + classFieldUsageOptionalThis + optionalSpace
            + "=" + optionalSpace + croatianChars + optionalSpace + ";?" + optionalSpace + "\\}";

    String getMethod = allOptionsAccessModifiersOptioanl + type + mandatorySpace + "(get|is)"+croatianChars + optionalSpace
            + "\\(" + optionalSpace + "\\)" + optionalSpace
            + "\\{" + optionalSpace + "return" + mandatorySpace + classFieldUsageOptionalThis + optionalSpace
            + ";?" + optionalSpace + "\\}";

    String throwsOption = "(throws"+mandatorySpace+croatianChars+"("+optionalSpace+","+optionalSpace+croatianChars+")*"+mandatorySpace+")?";
    String emptyConstructor = "(^|"+newLine+")"+spaceExcludeNewLine+"*"+optionalAccessModifiers + croatianChars + optionalSpace
            + "\\(" + optionalSpace + inputParameters + "?" + optionalSpace + "\\)" + optionalSpace+throwsOption
            + "\\{" + optionalSpace + constructorSuperCall + "?" + optionalSpace + "\\}";

    String emptyVoidFunctions = allOptionsAccessModifiersOptioanl + "void" + mandatorySpace + croatianChars + optionalSpace
            + "\\(" + optionalSpace + inputParameters + "?" + optionalSpace + "\\)" + optionalSpace+throwsOption
            + "\\{" + optionalSpace + "(return" + optionalSpace + ";?)?" + optionalSpace + "\\}";

    String emptyNonVoidFunction = allOptionsAccessModifiersOptioanl + type + mandatorySpace + croatianChars + optionalSpace
            + "\\(" + optionalSpace + inputParameters + "?" + optionalSpace + "\\)" + optionalSpace+throwsOption
            + "(\\{" + optionalSpace + "\\})";


    String loopsAndSimilar = "(for|while|switch)";
    String emptyElseIfBlockV1 = "((^|"+newLine+")"+spaceExcludeNewLine+"*)else"+mandatorySpace+"if" + optionalSpace
            + "\\(" + optionalSpace + "(("+croatianChar+"|[()!|&. =;:<>\\-\\+*/\"])+)?" + optionalSpace + "\\)" + optionalSpace
            + "\\{" + optionalSpace + "\\}";
    String emptyElseIfBlockV2 = "[}]{1,1}"+spaceExcludeNewLine+"else"+mandatorySpace+"if" + optionalSpace
            + "\\(" + optionalSpace + "(("+croatianChar+"|[()!|&. =;:<>\\-\\+*/\"])+)?" + optionalSpace + "\\)" + optionalSpace
            + "\\{" + optionalSpace + "\\}";

    String emptyBlockIfForWhileSwitch = "(^|"+newLine+")"+spaceExcludeNewLine+"*"+loopsAndSimilar + optionalSpace
            + "\\(" + optionalSpace + "(("+croatianChar+"|[()!|&. =;:<>\\-\\+*/\"])+)?" + optionalSpace + "\\)" + optionalSpace
            + "\\{" + optionalSpace + "\\}";

    String multiCatchParam = type+optionalSpace+"([|]"+optionalSpace+type+optionalSpace+")*"+croatianChars+optionalSpace;
    String emptyTryCatchFinallyStatement = "((try)|(try\\(TPLEXCLdummy TPLEXCLdummy=TPLEXCLdummy\\)))"+optionalSpace+"\\{"+optionalSpace+"\\}"+optionalSpace+
            "(catch"+optionalSpace+"\\("+multiCatchParam+"\\)"
            +optionalSpace+"\\{"+optionalSpace+"\\}"+optionalSpace+")+"
            +"finally"+optionalSpace+"\\{"+optionalSpace+"\\}";
    String multipleEmptyCatchStatement =  "(catch"+optionalSpace+"\\("+multiCatchParam+"\\)"
            +optionalSpace+"\\{"+optionalSpace+"\\}"+optionalSpace+"){2,}";
    String emptyFinalyStatement = "finally"+optionalSpace+"\\{"+optionalSpace+"\\}";
    String emptyElseStatement = "else"+optionalSpace+"\\{"+optionalSpace+"\\}";
    String emptyIfElse = "(^|"+newLine+")"+spaceExcludeNewLine+"*if"+ optionalSpace
            + "\\(" + optionalSpace + "(("+croatianChar+"|[()!|&. =;:<>\\-\\+*/\"])+)?" + optionalSpace + "\\)" + optionalSpace
            + "\\{" + optionalSpace + "\\}"+optionalSpace
            +"((else"+mandatorySpace+"if)"+ optionalSpace
            + "\\(" + optionalSpace + "(("+croatianChar+"|[()!|&. =;:<>\\-\\+*/\"])+)?" + optionalSpace + "\\)" + optionalSpace
            + "\\{" + optionalSpace + "\\}"+optionalSpace+")*"+optionalSpace
            +"else"+optionalSpace+"\\{"+optionalSpace+"\\}";
    String multipleEmptyIfStatements = "(^|"+newLine+")"+spaceExcludeNewLine+"*(if"+ optionalSpace
            + "\\(" + optionalSpace + "(("+croatianChar+"|[()!|&. =;:<>\\-\\+*/\"])+)?" + optionalSpace + "\\)" + optionalSpace
            + "\\{" + optionalSpace + "\\}"+optionalSpace+"){2,}";
    String annotationParamsChar = "([^{}()@])";
    String simpleAnnotation = "@" + optionalSpace + "("+croatianChar+"|\\.)+";
    String simpleAnnotationWithParams = "@" + optionalSpace + "("+croatianChar+"|\\.)+" + optionalSpace + "\\(" + optionalSpace+ "((\"|')"+optionalSpace+"#"+optionalSpace+"\\()?" + "("+annotationParamsChar+ "|[{}])*" + optionalSpace + "(\\)"+optionalSpace+"(\"|'))?\\)";
    String complexAnnotation = "@" + optionalSpace + "("+croatianChar+"|\\.)+" + optionalSpace + "\\(" + optionalSpace +"(\"|')?\\{([@()]|" + annotationParamsChar + ")+\\}(\"|')?" + optionalSpace + "\\)";

    String simpleFieldSetConstructorOrFunction = "(^|"+newLine+")"+spaceExcludeNewLine+"*"+allOptionsAccessModifiersOptioanl + "(void" + mandatorySpace + ")?" + croatianChars + optionalSpace
            + "\\(" + optionalSpace + inputParameters + "?" + optionalSpace + "\\)" + optionalSpace
            + "\\{" + optionalSpace
            + "(" + classFieldUsageMandatoryThis + optionalSpace + "=" + optionalSpace + croatianChars + optionalSpace + ";" + optionalSpace + ")+"
            + optionalSpace + "\\}";

    String oneLineComments = "(\\/\\/[^\n]*"+newLine+")|(("+whiteSpace+"|^)\\*[^\n\\/\\*]*"+newLine+")";
    String multiLineCommentLeftover = "("+whiteSpace+"|^)\\*[^\n\\/\\*]*"+newLine;
    String multiLineComments = "(\\/\\*[^\\/]*\\*\\/)";

    String stringValues = "(\"[^\"]*\")|('[^']*')";

    public String removePackageStatements(String content) {
        return content.replaceAll(packageStatement, "");
    }

    public String removeImportStatements(String content) {
        return content.replaceAll(importStatement, "");
    }

    public String removeNotInitializedClassFields(String content) {
        return content.replaceAll(nonInitializedFields, "");
    }

    public String removeSetMethods(String content) {
        return content.replaceAll(setMethod, "");
    }

    public String removeGetMethods(String content) {
        return content.replaceAll(getMethod, "");
    }

    public String removeEmptyVoidMethodsAndConstructors(String content) {
        content = inlineCatchTahtIsInSeparateLine(content);
        content = content.replaceAll(emptyVoidFunctions, "");
        return content.replaceAll(emptyConstructor, "\n");
    }

    private String inlineCatchTahtIsInSeparateLine(String content) {
        return content.replaceAll("\\}"+spaceExcludeNewLine+"*"+newLine+"+"+spaceExcludeNewLine+"*catch","} catch");
    }

    public String removeSimpleFieldsSetConstructorsOrFunctions(String content) {
        return content.replaceAll(simpleFieldSetConstructorOrFunction, "\n");
    }

    public String removeEmptyClasses(String content) {
        return content.replaceAll(emptyClass, "");
    }

    public String removeAnnotations(String content) {
        content = removeByCharRead(content, complexAnnotation);
        //System.err.println("complexAnotation");
        content = removeByCharRead(content, simpleAnnotationWithParams);
        //System.err.println("simpleAnotation");
        return removeByCharRead(content, simpleAnnotation);
    }

    private String removeByCharRead(String content, String regex){
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for(char c : content.toCharArray()){
            sb.append(c);
            if(c == '\n'){
                i++;
            }
            if(i>200){
                String lines = sb.toString();
                lines = lines.replaceAll(regex, "");
                sb = new StringBuilder();
                sb.append(lines);
                i=0;
            }
        }
        return sb.toString().replaceAll(regex, "");
    }

    public String removeLeftoverWhiteSpaces(String content) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for(char c : content.toCharArray()){
            sb.append(c);
            if(c == '\n'){
                i++;
            }
            if(i>100){
                String lines = sb.toString();
                lines = lines.replaceAll("(" + newLine + "("+spaceExcludeNewLine+")*" + newLine + "("+spaceExcludeNewLine+")*" + "){2,}", "\n");
                sb = new StringBuilder();
                sb.append(lines);
                i=0;
            }
        }

        return  sb.toString().replaceAll("(" + newLine + "("+spaceExcludeNewLine+")*" + newLine + "("+spaceExcludeNewLine+")*" + "){2,}", "\n");
        //return content.replaceAll("(" + newLine + "("+spaceExcludeNewLine+")*" + newLine + "("+spaceExcludeNewLine+")*" + "){2,}", "\n");
    }

    public String removeStringValues(String content){
        content = content.replaceAll("\\\\\"","'");
        return content.replaceAll(stringValues,"\"\"");
    };

    public String removeAnyEmptyMethod(String content){
        content = content.replaceAll(emptyVoidFunctions,"");
        content = content.replaceAll(emptyNonVoidFunction,"");
        return content;
    }

    public String removeEmptyLoopBlockAndSimilar(String content){
        for(int i=0;i<10;i++) {
            //System.out.println("------------ENDSTARTA-----------------\n"+content+"\n------------ENDA-----------------\n");
            content = content.replaceAll(emptyIfElse, "\n");
            //System.out.println("------------ENDSTARTA-----------------\n"+content+"\n------------ENDA-----------------\n");
            content = content.replaceAll(emptyBlockIfForWhileSwitch, "\n");

            for (int j = 0; j < 10; j++) {
                content = content.replaceAll(emptyElseIfBlockV2, "}");
                content = content.replaceAll(emptyElseIfBlockV1, "\n");
            }

            content = content.replaceAll(multipleEmptyIfStatements, "\n if (RCCdummy) {} \n");
            content = content.replaceAll(emptyTryCatchFinallyStatement, "\n");
            content = content.replaceAll(multipleEmptyCatchStatement, "catch (Exception e) {}\n");
        }
        return content;
    }

    public String removeEmptyElseFinallyStatement(String content){
        content = content.replaceAll(emptyFinalyStatement,"");
        content = content.replaceAll(emptyElseStatement,"");
        return content;
    }

    public String removeSimpleStatements(String content){
        //System.out.println("------------ENDSTARTB-----------------\n"+content+"\n------------ENDB-----------------\n");
        content=content.replaceAll(mandatorySpace+"("+croatianChar+"|[.])+"+optionalSpace+"[+*-]{2,2}"+optionalSpace+";","\n");
        //System.out.println("------------ENDSTARTB-----------------\n"+content+"\n------------ENDB-----------------\n");
        content=content.replaceAll(initStatemet,"\n");
        //content = content.replaceAll(verySimpleLineRemover,"");
        //content = content.replaceAll(simpleInitializatorStatemet,";");
        //System.out.println("------------ENDSTARTA-----------------\n"+content+"\n------------ENDA-----------------\n");
        //content = content.replaceAll(complexObjectInitializatorStatemnt,";");
        //System.out.println("------------ENDSTARTA-----------------\n"+content+"\n------------ENDA-----------------\n");
        //content = content.replaceAll(simpleObjectInitializatorStatemnt,";");
        //System.out.println("------------ENDSTARTA-----------------\n"+content+"\n------------ENDA-----------------\n");
        content = content.replaceAll(functionCallStatement2,"");
        //System.out.println("------------ENDSTARTB-----------------\n"+content+"\n------------ENDB-----------------\n");
        content = content.replaceAll(simpleIntializatorClassVariableStatement,"");
        /*for(int i=0;i<5;i++) {
            content = content.replaceAll(newLine + spaceExcludeNewLine + "*;" + spaceExcludeNewLine + "*" + newLine, "\n");
            content = content.replaceAll(newLine + spaceExcludeNewLine + "*;" + spaceExcludeNewLine + "*" + newLine, "\n");
            content = content.replaceAll("^" + spaceExcludeNewLine + "*;" + spaceExcludeNewLine + "*" + newLine, "\n");
            content = content.replaceAll(newLine + spaceExcludeNewLine + "*;" + spaceExcludeNewLine + "*" + "$", "\n");
            content = content.replaceAll(";" + optionalSpace + ";", ";");
            content = content.replaceAll("\\{" + optionalSpace + ";", "{");
            content = content.replaceAll("\\}" + optionalSpace + ";", "}");
            content = content.replaceAll("^" + optionalSpace + ";" + optionalSpace + "$", "");
        }*/
        //System.out.println("------------ENDSTARTB-----------------");
        //System.out.println(content);
        //System.out.println("------------ENDB-----------------");

        //System.out.println("------------ENDSTARTC-----------------");
        //System.out.println(content);
        //System.out.println("------------ENDC-----------------");
        return content;
    }

    public String removeTryWithResources(String content) {
        return content.replaceAll(mandatorySpace+"try"+optionalSpace+"\\([^{}]*\\)"+optionalSpace+"\\{","\ntry(TPLEXCLdummy TPLEXCLdummy=TPLEXCLdummy){");
    }

    public String removeComments(String content) {
        CommentsRemover cr = new CommentsRemover(content);
        return cr.run();
    }

    public String removeSyncronizedThis(String block) {
        block =     block.replaceAll(
                        "synchronized"+optionalSpace+"\\("+optionalSpace+"this"+optionalSpace+"\\)"+optionalSpace+"\\{",
                "if (syncTPLEXCLthis) {");
        return block;
    }


    public class CommentsRemover {
        StringBuilder sb = new StringBuilder();
        boolean singleLineComment = false;
        boolean multiLineComment = false;
        boolean beginsWithUnopenedComment = false;
        boolean insideOfString = false;
        boolean skipNext = false;
        char quoteType = '\n';
        int i;
        char c;
        String content;
        int contentLength;

        public CommentsRemover(String content){
            this.content = content;
            this.contentLength = content.length() - 1;
        }

        public String run() {
            checkIfStartsInsideOfMultilineComment();
            removeComments();
            checkIfEndsInsideOfMultilineComment();

            return sb.toString();
        }

        private void checkIfStartsInsideOfMultilineComment() {
            if(content.matches("^"+optionalSpace+"\\*[\\s\\S]*")) {
                multiLineComment = true;
                beginsWithUnopenedComment = true;
            }
        }

        private void checkIfEndsInsideOfMultilineComment() {
            if(multiLineComment && !beginsWithUnopenedComment)
                sb.append("/*");
        }

        private void removeComments() {
            for(i=0;i<=contentLength;i++){
                c = content.charAt(i);

                if(skipNext) {
                    skipNext = false;
                    continue;
                }

                if(isNotInComment())
                    handleQutedText();

                if(insideOfString)
                    sb.append(c);
                else
                    handleCode();
            }
        }

        private void handleQutedText() {
            if (!insideOfString && (c == '"' || c == '\'')) {
                insideOfString = true;
                quoteType = c;
            } else if(insideOfString && c == '\\') {
                writeEscapedCharaterAndMoveToNext(content);
            } else if (insideOfString && (quoteType == c)) {
                insideOfString = false;
            }
        }

        private void writeEscapedCharaterAndMoveToNext(String content) {
            sb.append(c);
            i++;
            c=content.charAt(i);
        }

        private void handleCode() {
            if (isFirstSlash()) {
                handleFirstSlash();
            } else if (isEndOfSingleLineComment()) {
                singleLineComment = false;
                sb.append(c);
            } else if (isEndOfMultilineComment()) {
                multiLineComment = false;
                skipNext = true;
                handleUnOpenedMultilineComment();
            } else if (isNotInComment()) {
                sb.append(c);
            }
        }

        private void handleFirstSlash() {
            if (i<contentLength && content.charAt(i + 1) == '/') {
                singleLineComment = true;
                skipNext = true;
            } else if (i<contentLength && content.charAt(i + 1) == '*') {
                multiLineComment = true;
                skipNext = true;
            } else {
                sb.append(c);
            }
        }

        private void handleUnOpenedMultilineComment() {
            if(beginsWithUnopenedComment){
                sb.append("*/");
                beginsWithUnopenedComment = false;
            }
        }

        private boolean isFirstSlash() {
            return !singleLineComment && !multiLineComment && c == '/';
        }

        private boolean isEndOfSingleLineComment() {
            return singleLineComment && c == '\n';
        }

        private boolean isEndOfMultilineComment() {
            return multiLineComment && c == '*' && i<contentLength && content.charAt(i + 1) == '/';
        }

        private boolean isNotInComment() {
            return !singleLineComment && !multiLineComment;
        }
    }
}
