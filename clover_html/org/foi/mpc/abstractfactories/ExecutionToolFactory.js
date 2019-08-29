var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":17,"id":1810,"methods":[],"name":"ExecutionToolFactory","sl":8},{"el":13,"id":1810,"methods":[{"el":12,"sc":9,"sl":10}],"name":"ExecutionToolFactory.ClassNameException","sl":9}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_15":{"methods":[{"sl":10}],"name":"techniqueNotCreateIfWrongNameIsGivenForPart","pass":true,"statements":[{"sl":11}]},"test_241":{"methods":[{"sl":10}],"name":"throwExceptionIfToolNameIsEmpty","pass":true,"statements":[{"sl":11}]},"test_257":{"methods":[{"sl":10}],"name":"ifNonExistingComboTechniqueNameIsGivenToCreateToolThrowException","pass":true,"statements":[{"sl":11}]},"test_55":{"methods":[{"sl":10}],"name":"throwExceptionIfToolNameIsWrongWhenCreating","pass":true,"statements":[{"sl":11}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [55, 15, 257, 241], [55, 15, 257, 241], [], [], [], [], [], []]
