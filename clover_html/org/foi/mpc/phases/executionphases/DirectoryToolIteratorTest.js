var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":299,"id":6336,"methods":[{"el":47,"sc":5,"sl":36},{"el":53,"sc":5,"sl":49},{"el":284,"sc":5,"sl":280}],"name":"DirectoryToolIteratorTest","sl":28},{"el":193,"id":6347,"methods":[{"el":62,"sc":9,"sl":59},{"el":68,"sc":9,"sl":64},{"el":73,"sc":9,"sl":70},{"el":80,"sc":9,"sl":75},{"el":89,"sc":9,"sl":82},{"el":107,"sc":9,"sl":91},{"el":125,"sc":9,"sl":109},{"el":143,"sc":9,"sl":127},{"el":158,"sc":9,"sl":145},{"el":176,"sc":9,"sl":160},{"el":192,"sc":9,"sl":178}],"name":"DirectoryToolIteratorTest.withFixedDirsAndNoRealCopy","sl":55},{"el":278,"id":6445,"methods":[{"el":204,"sc":9,"sl":197},{"el":223,"sc":9,"sl":206}],"name":"DirectoryToolIteratorTest.withRealFilesCopy","sl":195},{"el":277,"id":6465,"methods":[{"el":251,"sc":13,"sl":232},{"el":259,"sc":13,"sl":253},{"el":267,"sc":13,"sl":261},{"el":276,"sc":13,"sl":269}],"name":"DirectoryToolIteratorTest.withRealFilesCopy.sequentialOrIndividaulToolCalls","sl":225},{"el":298,"id":6499,"methods":[{"el":292,"sc":9,"sl":290},{"el":297,"sc":9,"sl":294}],"name":"DirectoryToolIteratorTest.DirectoryToolIteratorFakeWithNoRealDirCopy","sl":286}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_110":{"methods":[{"sl":269}],"name":"outputDirInSequentialIsLastToolDirList","pass":true,"statements":[{"sl":271},{"sl":272},{"sl":274},{"sl":275}]},"test_135":{"methods":[{"sl":70}],"name":"executeWithListOfToolsIsNull","pass":true,"statements":[{"sl":72}]},"test_234":{"methods":[{"sl":91},{"sl":280},{"sl":294}],"name":"processListWithMultipleElementsOnOneTool","pass":true,"statements":[{"sl":93},{"sl":94},{"sl":95},{"sl":96},{"sl":97},{"sl":98},{"sl":99},{"sl":100},{"sl":101},{"sl":103},{"sl":104},{"sl":105},{"sl":281},{"sl":282},{"sl":283}]},"test_30":{"methods":[{"sl":197}],"name":"wrongDirToProcessCopyFailed","pass":true,"statements":[{"sl":199},{"sl":200},{"sl":201},{"sl":202},{"sl":203}]},"test_31":{"methods":[{"sl":253}],"name":"actualCopyOfSourceDirWasPerformedIndividual","pass":true,"statements":[{"sl":255},{"sl":256},{"sl":257},{"sl":258}]},"test_425":{"methods":[{"sl":160}],"name":"ifToolHasExecutedOnDatasetThenSkipIt","pass":true,"statements":[{"sl":162},{"sl":163},{"sl":164},{"sl":165},{"sl":166},{"sl":167},{"sl":168},{"sl":169},{"sl":170},{"sl":171},{"sl":173},{"sl":174},{"sl":175}]},"test_446":{"methods":[{"sl":109},{"sl":280},{"sl":294}],"name":"runWasCalledOnTwoDiferentToolsForEveryDirectory","pass":true,"statements":[{"sl":111},{"sl":112},{"sl":113},{"sl":114},{"sl":115},{"sl":116},{"sl":117},{"sl":118},{"sl":119},{"sl":120},{"sl":121},{"sl":122},{"sl":123},{"sl":281},{"sl":282},{"sl":283}]},"test_471":{"methods":[{"sl":206}],"name":"actualCopyOfSourceDirWasPerformedParalel","pass":true,"statements":[{"sl":208},{"sl":209},{"sl":210},{"sl":211},{"sl":212},{"sl":213},{"sl":214},{"sl":215},{"sl":217},{"sl":218},{"sl":220},{"sl":221},{"sl":222}]},"test_480":{"methods":[{"sl":82}],"name":"executionToolListIsEmpty","pass":true,"statements":[{"sl":84},{"sl":85},{"sl":86},{"sl":87},{"sl":88}]},"test_482":{"methods":[{"sl":64}],"name":"workingDirListIsNull","pass":true,"statements":[{"sl":66},{"sl":67}]},"test_489":{"methods":[{"sl":261}],"name":"actualCopyOfSourceDirWasPerformedSequential","pass":true,"statements":[{"sl":263},{"sl":264},{"sl":265},{"sl":266}]},"test_521":{"methods":[{"sl":127},{"sl":280},{"sl":294}],"name":"runWasDoneByMultipleToolsAndMultipleDirs","pass":true,"statements":[{"sl":129},{"sl":130},{"sl":131},{"sl":132},{"sl":134},{"sl":135},{"sl":137},{"sl":139},{"sl":140},{"sl":141},{"sl":281},{"sl":282},{"sl":283}]},"test_633":{"methods":[{"sl":178},{"sl":294}],"name":"ifToolDirExistsButNotExecutedWorkNormaly","pass":true,"statements":[{"sl":180},{"sl":181},{"sl":182},{"sl":183},{"sl":184},{"sl":185},{"sl":186},{"sl":187},{"sl":188},{"sl":189},{"sl":190}]},"test_640":{"methods":[{"sl":145},{"sl":294}],"name":"sourceDirsForVariousToolsAreOk","pass":true,"statements":[{"sl":147},{"sl":148},{"sl":149},{"sl":150},{"sl":151},{"sl":152},{"sl":153},{"sl":154},{"sl":155},{"sl":156}]},"test_651":{"methods":[{"sl":75}],"name":"workingDirListIsEmpty","pass":true,"statements":[{"sl":77},{"sl":78},{"sl":79}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [482], [], [482], [482], [], [], [135], [], [135], [], [], [651], [], [651], [651], [651], [], [], [480], [], [480], [480], [480], [480], [480], [], [], [234], [], [234], [234], [234], [234], [234], [234], [234], [234], [234], [], [234], [234], [234], [], [], [], [446], [], [446], [446], [446], [446], [446], [446], [446], [446], [446], [446], [446], [446], [446], [], [], [], [521], [], [521], [521], [521], [521], [], [521], [521], [], [521], [], [521], [521], [521], [], [], [], [640], [], [640], [640], [640], [640], [640], [640], [640], [640], [640], [640], [], [], [], [425], [], [425], [425], [425], [425], [425], [425], [425], [425], [425], [425], [], [425], [425], [425], [], [], [633], [], [633], [633], [633], [633], [633], [633], [633], [633], [633], [633], [633], [], [], [], [], [], [], [30], [], [30], [30], [30], [30], [30], [], [], [471], [], [471], [471], [471], [471], [471], [471], [471], [471], [], [471], [471], [], [471], [471], [471], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [31], [], [31], [31], [31], [31], [], [], [489], [], [489], [489], [489], [489], [], [], [110], [], [110], [110], [], [110], [110], [], [], [], [], [521, 446, 234], [521, 446, 234], [521, 446, 234], [521, 446, 234], [], [], [], [], [], [], [], [], [], [], [640, 633, 521, 446, 234], [], [], [], [], []]
