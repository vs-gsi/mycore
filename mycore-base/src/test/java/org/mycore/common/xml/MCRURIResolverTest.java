package org.mycore.common.xml;

import org.junit.Assert;
import org.junit.Test;
import org.mycore.common.MCRTestCase;
import org.mycore.common.config.MCRConfigurationDir;

import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class MCRURIResolverTest extends MCRTestCase {

    public static final String TEST_DEVELOPER_FOLDER_STRING = "/home/workspace";

    @Test
    public void testGetParentDirectoryResourceURI() {
        Map<String, String> testData = new LinkedHashMap<>();

        testData.put("/root/.m2/repository/some/directory/some.jar!/xsl/directory/myfile.xsl",
            "resource:xsl/directory/");

        testData.put("/root/.m2/repository/some/directory/some.jar!/xsl/myfile.xsl",
            "resource:xsl/");

        String configurationXSLDirectory
            = MCRConfigurationDir.getConfigurationDirectory().toPath()
                .resolve("resources")
                .resolve("xsl")
                .toFile()
                .toURI()
                .toString();
        testData.put(configurationXSLDirectory + "/mir-accesskey-utils.xsl", "resource:xsl/");

        testData.put(configurationXSLDirectory + "/directory/mir-accesskey-utils.xsl",
            "resource:xsl/directory/");

        testData.put(Paths.get(TEST_DEVELOPER_FOLDER_STRING).toAbsolutePath().toFile().toURI()
            + "/directory/mir-accesskey-utils.xsl", "resource:directory/");

        for (Map.Entry<String, String> entry : testData.entrySet()) {
            String result = MCRURIResolver.getParentDirectoryResourceURI(entry.getKey());
            Assert.assertEquals(entry.getValue(), result);
        }
    }

    @Override
    protected Map<String, String> getTestProperties() {
        Map<String, String> properties = super.getTestProperties();

        properties.put("MCR.Developer.Resource.Override", TEST_DEVELOPER_FOLDER_STRING);

        return properties;
    }
}
