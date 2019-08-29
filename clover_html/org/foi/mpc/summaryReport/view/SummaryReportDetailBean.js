var clover = new Object();

// JSON: {classes : [{name, id, sl, el,  methods : [{sl, el}, ...]}, ...]}
clover.pageData = {"classes":[{"el":79,"id":13251,"methods":[{"el":30,"sc":5,"sl":23},{"el":50,"sc":5,"sl":32},{"el":58,"sc":5,"sl":52},{"el":62,"sc":5,"sl":60},{"el":66,"sc":5,"sl":64},{"el":70,"sc":5,"sl":68},{"el":74,"sc":5,"sl":72},{"el":78,"sc":5,"sl":76}],"name":"SummaryReportDetailBean","sl":17}]}

// JSON: {test_ID : {"methods": [ID1, ID2, ID3...], "name" : "testXXX() void"}, ...};
clover.testTargets = {"test_134":{"methods":[{"sl":76}],"name":"beanCreatesUseCase","pass":true,"statements":[{"sl":77}]},"test_181":{"methods":[{"sl":68}],"name":"beanCreatesPresenter","pass":true,"statements":[{"sl":69}]},"test_264":{"methods":[{"sl":60}],"name":"beanCreatesController","pass":true,"statements":[{"sl":61}]},"test_292":{"methods":[{"sl":60},{"sl":68}],"name":"beanPassesPresenterToController","pass":true,"statements":[{"sl":61},{"sl":69}]},"test_461":{"methods":[{"sl":60},{"sl":76}],"name":"beanPassesUseCaseToController","pass":true,"statements":[{"sl":61},{"sl":77}]},"test_524":{"methods":[{"sl":64}],"name":"beanCreatesViewModel","pass":true,"statements":[{"sl":65}]},"test_603":{"methods":[{"sl":60},{"sl":64}],"name":"beanPassesViewModelToController","pass":true,"statements":[{"sl":61},{"sl":65}]}}

// JSON: { lines : [{tests : [testid1, testid2, testid3, ...]}, ...]};
clover.srcFileLines = [[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [292, 461, 603, 264], [292, 461, 603, 264], [], [], [603, 524], [603, 524], [], [], [181, 292], [181, 292], [], [], [], [], [], [], [461, 134], [461, 134], [], []]
