//Evaluate flag from environnement variables
const evaluateFlag = (value) => {
    return (value === 'true');
};

//For logging
const logEnable = evaluateFlag(process.env.LOG_ENABLE);
const apiCheckEnable = evaluateFlag(process.env.API_CHECK_ENABLE)
const headerApiKey = process.env.API_KEY_HEADER;
const apiKey = process.env.API_KEY;;

//Build policy for API access
const getPolicy = (allow, principalId, routeArn) => {
    const policy = {
        principalId: typeof(principalId) != 'undefined' ? principalId : 'unauthorized-user',
        policyDocument: {
            Version: "2012-10-17",
            Statement: [
                {
                    Action: "execute-api:Invoke",
                    Effect: allow ? "Allow" : "Deny",
                    Resource: routeArn
                }
            ]
        },
        usageIdentifierKey: apiKey
    };
    return policy;
};

//Check all expected headers are present in the request
const checkHeaders = (event, headerList) => {
    let checkIsOk = true;
    for(let id=0; id<headerList.length; id++) {
        checkIsOk = typeof(event.headers[headerList[id]]) != 'undefined';
        if(!checkIsOk) {
            console.log('ERROR: header is missing: ' + headerList[id]);
            break;
        }
    }
    return checkIsOk;
};

//Check API key
const checkApiKeys = async(event) => {
    //Headers to be present in request
    const headers = [headerApiKey];
    //First check headers are present in the request
    if(!checkHeaders(event, headers)) {
        console.log('ERROR: checkHeaders failed');
        return false;
    }
    const requestApiKey = event.headers[headerApiKey];
    //if(logEnable) console.log("requestApiKey: " + requestApiKey);
    return (requestApiKey === apiKey);
};

//Lambda authorizer handler
export const handler = async (event, context) => {
    //console.log('Received event:', JSON.stringify(event, null, 2));
    //Get route arn
    const routeKey = event.routeKey;
    if(logEnable) console.log("routeKey: " + routeKey);
    const routeArn = event.routeArn;
    if(logEnable) console.log("routeArn: " + routeArn);
    const principalId = 'EMK';
    if(!apiCheckEnable) {
        if(logEnable) console.log('API check is disable. Allow access');
        return getPolicy(true, principalId, routeArn, API_KEY);
    }
    //Build response policy based on the result of API keys check
    try {
        const checkApiKeysIsOk = await checkApiKeys(event);
        if(logEnable)
            checkApiKeysIsOk ? console.log('Allow access') : console.log('Deny access');
        return getPolicy(checkApiKeysIsOk, principalId, routeArn);
    } catch(error) {
        console.log('ERROR: checkApiKeys failed. ' + error.message);
        return getPolicy(false, null, routeArn, "");
    }
};
