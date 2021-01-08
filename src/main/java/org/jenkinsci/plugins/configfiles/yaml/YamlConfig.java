/*
 The MIT License

 Copyright (c) 2011, Dominik Bartholdi, Olivier Lamy

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */
package org.jenkinsci.plugins.configfiles.yaml;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import jenkins.model.Jenkins;

import org.jenkinsci.lib.configprovider.AbstractConfigProviderImpl;
import org.jenkinsci.lib.configprovider.model.Config;
import org.jenkinsci.lib.configprovider.model.ContentType;
import org.jenkinsci.plugins.configfiles.Messages;
import org.kohsuke.stapler.DataBoundConstructor;

public class YamlConfig extends Config {
    private static final long serialVersionUID = 1L;

    @DataBoundConstructor
    public YamlConfig(String id, String name, String comment, String content) {
        super(id, name, comment, content);
    }

    public YamlConfig(String id, String name, String comment, String content, String providerId) {
        super(id, name, comment, content, providerId);
    }

    @Extension(ordinal = 609)
    public static class YamlConfigProvider extends AbstractConfigProviderImpl {

        public YamlConfigProvider() {
            load();
        }

        @Override
        public ContentType getContentType() {
            return new ContentType() {
                public String getCmMode() {
                    return "yaml";
                }

                public String getMime() {
                    return "text/x-yaml";
                }
            };
        }

        @Override
        public String getDisplayName() {
            return Messages.yaml_provider_name();
        }

        @NonNull
        @Override
        public YamlConfig newConfig(@NonNull String id) {
            return new YamlConfig(id,
                Messages.YamlConfig_SettingsName(),
                Messages.YamlConfig_SettingsComment(),
                "",
                getProviderId());
        }

        // ======================
        // start stuff for backward compatibility
        protected transient String ID_PREFIX;


        @Override
        protected String getXmlFileName() {
            return "custom-config-files.xml";
        }


        static {
            Jenkins.XSTREAM.alias("org.jenkinsci.plugins.configfiles.yaml.YamlConfigProvider", YamlConfigProvider.class);
        }
        // end stuff for backward compatibility
        // ======================

    }

}
