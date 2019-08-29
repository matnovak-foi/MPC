var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":71,"id":2926,"methods":[{"el":26,"sc":5,"sl":20},{"el":30,"sc":5,"sl":28},{"el":42,"sc":5,"sl":32},{"el":46,"sc":5,"sl":44},{"el":50,"sc":5,"sl":48},{"el":54,"sc":5,"sl":52},{"el":58,"sc":5,"sl":56},{"el":62,"sc":5,"sl":60},{"el":66,"sc":5,"sl":64},{"el":70,"sc":5,"sl":68}],"name":"ExecutionPhase","sl":11},{"el":12,"id":2926,"methods":[],"name":"ExecutionPhase.PhaseNotInstanciatedException","sl":12}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_126":{"methods":[{"sl":28},{"sl":32},{"sl":52}],"name":"runWasDoneByMultipleToolsAndMultipleDirs","pass":true,"statements":[{"sl":29},{"sl":33},{"sl":36},{"sl":39},{"sl":53}]},"test_133":{"methods":[{"sl":32},{"sl":52}],"name":"executeWithListOfToolsIsNull","pass":true,"statements":[{"sl":33},{"sl":34},{"sl":53}]},"test_140":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"useCaseCreatesCorrectToolInResponse","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]},"test_152":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"fromThreeCallsFirstIsBest","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_158":{"methods":[{"sl":20},{"sl":28},{"sl":44},{"sl":52},{"sl":56}],"name":"responseIsOkWhenInputIsOk","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":45},{"sl":53},{"sl":57}]},"test_159":{"methods":[{"sl":32}],"name":"workingDirListIsEmpty","pass":true,"statements":[{"sl":33},{"sl":36},{"sl":37}]},"test_165":{"methods":[{"sl":32},{"sl":52}],"name":"executeWithListOfToolsIsNull","pass":true,"statements":[{"sl":33},{"sl":34},{"sl":53}]},"test_167":{"methods":[{"sl":32}],"name":"workingDirListIsEmpty","pass":true,"statements":[{"sl":33},{"sl":36},{"sl":39}]},"test_176":{"methods":[{"sl":32}],"name":"inputDirectoryIsEmpty_workingDirHasEmptyPreprocessDir","pass":true,"statements":[{"sl":33},{"sl":36},{"sl":37}]},"test_183":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"useCaseCalulatesStatisticMeasures","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]},"test_185":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"useCaseCreatesCorrectTechniquesInResponse","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]},"test_19":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"useCaseCreatesPreparer","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]},"test_203":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"generatesCorrectOptimalToolParams","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_209":{"methods":[{"sl":20}],"name":"sourceDirDoesNotExist","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24}]},"test_224":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"generatesCorrectBaseToolParamsForSIM","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_23":{"methods":[{"sl":20}],"name":"inputDirectoryIsEmpty_workingDirHasEmptyPhaseDir","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25}]},"test_247":{"methods":[{"sl":32}],"name":"executionToolListIsEmpty","pass":true,"statements":[{"sl":33},{"sl":36},{"sl":37}]},"test_252":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"readerFoundMatchForTechniqueThatIsNotRequested","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]},"test_26":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"useCaseReturnsErrorBecouseMatchDirNameIsDifferentFromAnyTechnique","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]},"test_270":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"findCorrectSimilarityOnFirstRun","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_273":{"methods":[{"sl":20}],"name":"sourceDirIsNull","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24}]},"test_283":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"useCaseCreatesCorrectToolInResponse","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]},"test_302":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"useCaseCreatesPreparer","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]},"test_306":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"noSimilarityIsBestClosestIsFirst","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_311":{"methods":[{"sl":32}],"name":"inputDirectoryIsEmpty_workingDirHasEmptyPreprocessDir","pass":true,"statements":[{"sl":33},{"sl":36},{"sl":39}]},"test_347":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"invalidCalibrationCaseDirName","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_351":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"PPreportForTestPairWithJPlag","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]},"test_354":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"readerFoundMatchForTechniqueThatIsNotRequested","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]},"test_376":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"invalidCalibrationCaseDirName","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_399":{"methods":[{"sl":28},{"sl":32}],"name":"workingDirListIsNull","pass":true,"statements":[{"sl":29},{"sl":33},{"sl":36},{"sl":37}]},"test_410":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"noCorrectMatchRunAllIterations","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_42":{"methods":[{"sl":20}],"name":"workingDirDoesNotExist","pass":true,"statements":[{"sl":21}]},"test_428":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"generatesCorrectBaseToolParamsIfMatchIsZero","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_432":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"noSimilarityIsBestClosestIsThird","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_437":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"canRunMultipleDetectionOnStudentsDirs","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]},"test_441":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"runDetectionWithComboTechnique","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]},"test_449":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"generatesCorrectCalibratedToolParams","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_452":{"methods":[{"sl":20}],"name":"workingDirOfPhaseIsNotEmptyNoError","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25}]},"test_473":{"methods":[{"sl":28},{"sl":32},{"sl":52}],"name":"outputDirOfFirstIsInputDirOfSecond","pass":true,"statements":[{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":53}]},"test_486":{"methods":[{"sl":32}],"name":"executionToolListIsEmpty","pass":true,"statements":[{"sl":33},{"sl":36},{"sl":39}]},"test_488":{"methods":[{"sl":28},{"sl":32},{"sl":52}],"name":"runWasDoneByMultipleToolsAndMultipleDirs","pass":true,"statements":[{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":53}]},"test_49":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"useCaseCreatesCorrectTechniqueInResponse","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]},"test_494":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"generateSimilarityIfIsZero","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_503":{"methods":[{"sl":20},{"sl":28},{"sl":44},{"sl":52},{"sl":56}],"name":"createsCorrectPhaseRunner","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":45},{"sl":53},{"sl":57}]},"test_513":{"methods":[{"sl":20},{"sl":28},{"sl":44},{"sl":52},{"sl":56}],"name":"enablesCorrectPhasesToRun","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":45},{"sl":53},{"sl":57}]},"test_516":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"noSimilarityIsBestClosestIsSecond","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_519":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"useCaseRuns_PrepareTools_SelectedTool_Techniques_CorrectNumberOfTimes","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]},"test_534":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"PPreportForTestPairCanBeRunTwice","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]},"test_540":{"methods":[{"sl":20}],"name":"workingDirIsNull","pass":true,"statements":[{"sl":21}]},"test_543":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"useCaseCallsDirectoryPreparerWithCorrectInputDirDepthAndDir","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]},"test_547":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"similarityDetectionToolCalibratorWasCalled","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_557":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"useCaseRuns_PrepareTools_SelectedTool_Techniques_CorrectNumberOfTimes","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]},"test_57":{"methods":[{"sl":28},{"sl":32}],"name":"workingDirListIsNull","pass":true,"statements":[{"sl":29},{"sl":33},{"sl":36},{"sl":39}]},"test_570":{"methods":[{"sl":20},{"sl":44},{"sl":52},{"sl":56},{"sl":60},{"sl":64},{"sl":68}],"name":"canCreateDefaultSetUpForPreparePhase","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":45},{"sl":53},{"sl":57},{"sl":61},{"sl":65},{"sl":69}]},"test_584":{"methods":[{"sl":28},{"sl":32},{"sl":52}],"name":"copiesInputDirectoryWithMultipleCoursesYearsAndAssignments","pass":true,"statements":[{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":53}]},"test_591":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"invalidCalibrationCaseDirName","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_6":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"useCaseCallsDirectoryPreparerWithCorrectInputDirDepthAndDir","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]},"test_605":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"findCorrectSimilarityOnFirstRun","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_613":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"responseModelIsPassedToThePresenter","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]},"test_622":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56},{"sl":64}],"name":"createCorrectPhaseRunnerForBaseTool","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57},{"sl":65}]},"test_632":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"findCorrectSimilarity","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_634":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"fromThreeCallsThirdIsBest","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_636":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56},{"sl":60}],"name":"canRunAllToolsOnAllTehcniques","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57},{"sl":61}]},"test_642":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":48},{"sl":52},{"sl":56}],"name":"calibrationSIMTestWorksCase1","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":39},{"sl":49},{"sl":53},{"sl":57}]},"test_665":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"toolsAreRunOnCorrectInputDirs","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]},"test_667":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"findCorrectSimilarityOnSecondRun","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_679":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":48},{"sl":52},{"sl":56}],"name":"calibrationSIMTestSelfWorksCase","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":39},{"sl":49},{"sl":53},{"sl":57}]},"test_707":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"noCorrectMatchRunAll29Iterations","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_718":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":48},{"sl":52},{"sl":56}],"name":"calibrationSIMTestWorksCase2","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":39},{"sl":49},{"sl":53},{"sl":57}]},"test_721":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56},{"sl":60}],"name":"canRunCombinationsOfTechniques","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57},{"sl":61}]},"test_727":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"useCaseCreatesProcessedPlagiarizedInfo","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]},"test_742":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"useCaseCreatesCorrectInfoForStudents","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]},"test_752":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"fromThreeCallsSecondIsBest","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_753":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"generatesCorrectBaseToolParamsForJPlag","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_758":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"generatesCorrectAllComboParamsDiff","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_84":{"methods":[{"sl":28},{"sl":32},{"sl":52}],"name":"runWasDoneByMultipleToolsAndMultipleDirs","pass":true,"statements":[{"sl":29},{"sl":33},{"sl":36},{"sl":39},{"sl":53}]},"test_94":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"useCaseCreatesCorrectInfoForTechniques","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]},"test_96":{"methods":[{"sl":20},{"sl":28},{"sl":52},{"sl":56}],"name":"findCorrectSimilarityOnSecondRun","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":53},{"sl":57}]},"test_98":{"methods":[{"sl":20},{"sl":28},{"sl":32},{"sl":44},{"sl":48},{"sl":52},{"sl":56}],"name":"useCaseWorksOkWithTechniquesContainingOtherTechniqueName","pass":true,"statements":[{"sl":21},{"sl":22},{"sl":23},{"sl":24},{"sl":25},{"sl":29},{"sl":33},{"sl":36},{"sl":37},{"sl":39},{"sl":45},{"sl":49},{"sl":53},{"sl":57}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [26, 209, 224, 152, 494, 252, 96, 6, 428, 437, 449, 665, 19, 721, 306, 503, 753, 718, 707, 605, 302, 642, 94, 183, 376, 23, 347, 636, 758, 410, 547, 742, 591, 543, 679, 432, 203, 158, 613, 622, 42, 185, 351, 49, 513, 534, 441, 519, 452, 557, 516, 570, 727, 752, 354, 540, 140, 283, 634, 667, 632, 273, 98, 270], [26, 209, 224, 152, 494, 252, 96, 6, 428, 437, 449, 665, 19, 721, 306, 503, 753, 718, 707, 605, 302, 642, 94, 183, 376, 23, 347, 636, 758, 410, 547, 742, 591, 543, 679, 432, 203, 158, 613, 622, 42, 185, 351, 49, 513, 534, 441, 519, 452, 557, 516, 570, 727, 752, 354, 540, 140, 283, 634, 667, 632, 273, 98, 270], [26, 209, 224, 152, 494, 252, 96, 6, 428, 437, 449, 665, 19, 721, 306, 503, 753, 718, 707, 605, 302, 642, 94, 183, 376, 23, 347, 636, 758, 410, 547, 742, 591, 543, 679, 432, 203, 158, 613, 622, 185, 351, 49, 513, 534, 441, 519, 452, 557, 516, 570, 727, 752, 354, 140, 283, 634, 667, 632, 273, 98, 270], [26, 209, 224, 152, 494, 252, 96, 6, 428, 437, 449, 665, 19, 721, 306, 503, 753, 718, 707, 605, 302, 642, 94, 183, 376, 23, 347, 636, 758, 410, 547, 742, 591, 543, 679, 432, 203, 158, 613, 622, 185, 351, 49, 513, 534, 441, 519, 452, 557, 516, 570, 727, 752, 354, 140, 283, 634, 667, 632, 273, 98, 270], [26, 209, 224, 152, 494, 252, 96, 6, 428, 437, 449, 665, 19, 721, 306, 503, 753, 718, 707, 605, 302, 642, 94, 183, 376, 23, 347, 636, 758, 410, 547, 742, 591, 543, 679, 432, 203, 158, 613, 622, 185, 351, 49, 513, 534, 441, 519, 452, 557, 516, 570, 727, 752, 354, 140, 283, 634, 667, 632, 273, 98, 270], [26, 224, 152, 494, 252, 96, 6, 428, 437, 449, 665, 19, 721, 306, 503, 753, 718, 707, 605, 302, 642, 94, 183, 376, 23, 347, 636, 758, 410, 547, 742, 591, 543, 679, 432, 203, 158, 613, 622, 185, 351, 49, 513, 534, 441, 519, 452, 557, 516, 570, 727, 752, 354, 140, 283, 634, 667, 632, 98, 270], [], [], [26, 224, 152, 494, 252, 96, 6, 428, 437, 449, 665, 19, 721, 306, 503, 753, 488, 718, 707, 84, 605, 302, 642, 94, 183, 376, 126, 347, 636, 758, 584, 410, 547, 742, 591, 543, 679, 432, 203, 158, 613, 622, 185, 351, 49, 57, 513, 534, 399, 441, 519, 557, 516, 727, 752, 354, 140, 283, 634, 667, 632, 473, 98, 270], [26, 224, 152, 494, 252, 96, 6, 428, 437, 449, 665, 19, 721, 306, 503, 753, 488, 718, 707, 84, 605, 302, 642, 94, 183, 376, 126, 347, 636, 758, 584, 410, 547, 742, 591, 543, 679, 432, 203, 158, 613, 622, 185, 351, 49, 57, 513, 534, 399, 441, 519, 557, 516, 727, 752, 354, 140, 283, 634, 667, 632, 473, 98, 270], [], [], [26, 252, 6, 167, 176, 437, 665, 19, 721, 311, 159, 488, 718, 84, 486, 302, 642, 94, 183, 126, 636, 584, 742, 543, 679, 613, 185, 351, 49, 57, 534, 399, 165, 247, 441, 519, 557, 727, 354, 140, 283, 133, 473, 98], [26, 252, 6, 167, 176, 437, 665, 19, 721, 311, 159, 488, 718, 84, 486, 302, 642, 94, 183, 126, 636, 584, 742, 543, 679, 613, 185, 351, 49, 57, 534, 399, 165, 247, 441, 519, 557, 727, 354, 140, 283, 133, 473, 98], [165, 133], [], [26, 252, 6, 167, 176, 437, 665, 19, 721, 311, 159, 488, 718, 84, 486, 302, 642, 94, 183, 126, 636, 584, 742, 543, 679, 613, 185, 351, 49, 57, 534, 399, 247, 441, 519, 557, 727, 354, 140, 283, 473, 98], [26, 252, 6, 176, 437, 665, 19, 721, 159, 488, 302, 94, 183, 636, 584, 742, 543, 613, 185, 351, 49, 534, 399, 247, 441, 519, 557, 727, 354, 140, 283, 473, 98], [], [26, 252, 6, 167, 437, 665, 19, 721, 311, 718, 84, 486, 302, 642, 94, 183, 126, 636, 742, 543, 679, 613, 185, 351, 49, 57, 534, 441, 519, 557, 727, 354, 140, 283, 98], [], [], [], [], [26, 252, 6, 437, 665, 19, 721, 503, 302, 94, 183, 636, 742, 543, 158, 613, 185, 351, 49, 513, 534, 441, 519, 557, 570, 727, 354, 140, 283, 98], [26, 252, 6, 437, 665, 19, 721, 503, 302, 94, 183, 636, 742, 543, 158, 613, 185, 351, 49, 513, 534, 441, 519, 557, 570, 727, 354, 140, 283, 98], [], [], [26, 252, 6, 437, 665, 19, 721, 718, 302, 642, 94, 183, 636, 742, 543, 679, 613, 185, 351, 49, 534, 441, 519, 557, 727, 354, 140, 283, 98], [26, 252, 6, 437, 665, 19, 721, 718, 302, 642, 94, 183, 636, 742, 543, 679, 613, 185, 351, 49, 534, 441, 519, 557, 727, 354, 140, 283, 98], [], [], [26, 224, 152, 494, 252, 96, 6, 428, 437, 449, 665, 19, 721, 306, 503, 753, 488, 718, 707, 84, 605, 302, 642, 94, 183, 376, 126, 347, 636, 758, 584, 410, 547, 742, 591, 543, 679, 432, 203, 158, 613, 622, 185, 351, 49, 513, 534, 165, 441, 519, 557, 516, 570, 727, 752, 354, 140, 283, 634, 667, 632, 133, 473, 98, 270], [26, 224, 152, 494, 252, 96, 6, 428, 437, 449, 665, 19, 721, 306, 503, 753, 488, 718, 707, 84, 605, 302, 642, 94, 183, 376, 126, 347, 636, 758, 584, 410, 547, 742, 591, 543, 679, 432, 203, 158, 613, 622, 185, 351, 49, 513, 534, 165, 441, 519, 557, 516, 570, 727, 752, 354, 140, 283, 634, 667, 632, 133, 473, 98, 270], [], [], [26, 224, 152, 494, 252, 96, 6, 428, 437, 449, 665, 19, 721, 306, 503, 753, 718, 707, 605, 302, 642, 94, 183, 376, 347, 636, 758, 410, 547, 742, 591, 543, 679, 432, 203, 158, 613, 622, 185, 351, 49, 513, 534, 441, 519, 557, 516, 570, 727, 752, 354, 140, 283, 634, 667, 632, 98, 270], [26, 224, 152, 494, 252, 96, 6, 428, 437, 449, 665, 19, 721, 306, 503, 753, 718, 707, 605, 302, 642, 94, 183, 376, 347, 636, 758, 410, 547, 742, 591, 543, 679, 432, 203, 158, 613, 622, 185, 351, 49, 513, 534, 441, 519, 557, 516, 570, 727, 752, 354, 140, 283, 634, 667, 632, 98, 270], [], [], [721, 636, 570], [721, 636, 570], [], [], [622, 570], [622, 570], [], [], [570], [570], [], []]
