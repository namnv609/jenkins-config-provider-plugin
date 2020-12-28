package org.jenkinsci.plugins.configfiles.properties.security;

import com.cloudbees.plugins.credentials.common.StandardUsernameCredentials;
import com.cloudbees.plugins.credentials.common.StandardUsernameListBoxModel;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.model.ItemGroup;
import hudson.model.Queue;
import hudson.security.ACL;
import hudson.security.AccessControlled;
import hudson.util.ListBoxModel;
import jenkins.model.Jenkins;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class PropertiesCredentialMapping extends AbstractDescribableImpl<PropertiesCredentialMapping> implements Serializable {

    private final String propertyKey;
    private final String credentialsId;

    @DataBoundConstructor
    public PropertiesCredentialMapping(String propertyKey, String credentialsId) {
        this.propertyKey = propertyKey;
        this.credentialsId = credentialsId;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public String getCredentialsId() {
        return credentialsId;
    }

    public Descriptor<PropertiesCredentialMapping> getDescriptor() {
        return DESCRIPTOR;
    }

    private static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

    @Extension
    public static class DescriptorImpl extends Descriptor<PropertiesCredentialMapping> {

        public ListBoxModel doFillCredentialsIdItems(@AncestorInPath ItemGroup context, @QueryParameter String propertyKey) {
            AccessControlled _context = (context instanceof AccessControlled ? (AccessControlled) context : Jenkins.get());
            if (_context == null || !_context.hasPermission(Item.CONFIGURE)) {
                return new StandardUsernameListBoxModel().includeCurrentValue(propertyKey);
            }

            List<DomainRequirement> domainRequirements = Collections.emptyList();
            if (StringUtils.isNotBlank(propertyKey)) {
                domainRequirements = Collections.singletonList(new PropertyKeyRequirement(propertyKey));
            }

            // @formatter:off
            return new StandardUsernameListBoxModel().includeAs(
                    context instanceof Queue.Task ? ((Queue.Task) context).getDefaultAuthentication() : ACL.SYSTEM,
                    context,
                    StandardUsernameCredentials.class,
                    domainRequirements
            )
                    .includeCurrentValue(propertyKey);
            // @formatter:on
        }

    }

}
