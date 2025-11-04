package org.etyp.authorizer.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.*;


public class AwsUtils {

   private final static Logger LOGGER = LoggerFactory.getLogger(AwsUtils.class);

   public static String getParameterValue(Region region, String parameterName) {

      try ( SsmClient ssmClient = SsmClient.builder().region(region).build() ) {

         LOGGER.info("Ssm client build OK");

         GetParameterRequest parameterRequest = GetParameterRequest.builder()
                 .name(parameterName)
                 .withDecryption(true)
                 .build();
         LOGGER.info("Ssm request build OK");

         GetParameterResponse parameterResponse = ssmClient.getParameter(parameterRequest);
         Parameter parameter = parameterResponse.parameter();

         if(parameter != null) {
            LOGGER.info("Ssm parameterResponse OK");
            return parameter.value();
         } else {
            LOGGER.info("Ssm parameterResponse FAILED");
            return null;
         }

      } catch (SsmException e) {
         LOGGER.info("ERROR in getParameter: " + e);
         return null;
      }

   }

}
