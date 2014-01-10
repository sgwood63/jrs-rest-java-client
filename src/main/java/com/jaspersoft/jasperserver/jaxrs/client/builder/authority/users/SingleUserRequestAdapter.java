package com.jaspersoft.jasperserver.jaxrs.client.builder.authority.users;

import com.jaspersoft.jasperserver.dto.authority.ClientUser;
import com.jaspersoft.jasperserver.dto.authority.ClientUserAttribute;
import com.jaspersoft.jasperserver.dto.authority.UserAttributesListWrapper;
import com.jaspersoft.jasperserver.jaxrs.client.builder.AuthenticationCredentials;
import com.jaspersoft.jasperserver.jaxrs.client.builder.JerseyRequestBuilder;
import com.jaspersoft.jasperserver.jaxrs.client.builder.OperationResult;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class SingleUserRequestAdapter {

    private final JerseyRequestBuilder<ClientUser> builder;
    private AuthenticationCredentials credentials;

    private String username;


    public SingleUserRequestAdapter(AuthenticationCredentials credentials, String username) {
        this.credentials = credentials;
        this.builder = new JerseyRequestBuilder<ClientUser>(credentials, ClientUser.class);
        this.builder
                .setPath("/users")
                .setPath(username);
        this.username = username;
    }

    public SingleAttributeInterfaceAdapter attribute(String attributeName){
        JerseyRequestBuilder<ClientUserAttribute> builder =
                new JerseyRequestBuilder<ClientUserAttribute>(credentials, ClientUserAttribute.class);
        builder
                .setPath("/users")
                .setPath(username)
                .setPath("/attributes")
                .setPath(attributeName);
        return new SingleAttributeInterfaceAdapter(builder);
    }

    public BatchAttributeInterfaceAdapter attributes(){
        JerseyRequestBuilder<UserAttributesListWrapper> builder =
                new JerseyRequestBuilder<UserAttributesListWrapper>(credentials, UserAttributesListWrapper.class);
        builder
                .setPath("/users")
                .setPath(username)
                .setPath("/attributes");
        return new BatchAttributeInterfaceAdapter(builder);
    }

    public OperationResult<ClientUser> get(){
        return builder.get();
    }

    public OperationResult<ClientUser> createOrUpdate(ClientUser user){
        return builder.put(user);
    }

    public OperationResult<ClientUser> delete(){
        return builder.delete();
    }

    public class SingleAttributeInterfaceAdapter {

        private JerseyRequestBuilder<ClientUserAttribute> builder;

        public SingleAttributeInterfaceAdapter(JerseyRequestBuilder<ClientUserAttribute> builder){
            this.builder = builder;
        }

        public OperationResult<ClientUserAttribute> get(){
            return builder.get();
        }

        public OperationResult<ClientUserAttribute> createOrUpdate(ClientUserAttribute attribute){
            return builder.put(attribute);
        }

    }

    public class BatchAttributeInterfaceAdapter {

        private JerseyRequestBuilder<UserAttributesListWrapper> builder;
        private MultivaluedMap<String, String> params;

        public BatchAttributeInterfaceAdapter(JerseyRequestBuilder<UserAttributesListWrapper> builder){
            this.builder = builder;
            params = new MultivaluedHashMap<String, String>();
        }

        public BatchAttributeInterfaceAdapter param(UsersAttributesParameter usersAttributesParam, String value){
            params.add(usersAttributesParam.getParamName(), value);
            return this;
        }

        public OperationResult<UserAttributesListWrapper> get(){
            builder.addParams(params);
            return builder.get();
        }

        public OperationResult<UserAttributesListWrapper> createOrUpdate(UserAttributesListWrapper attributesList){

            return builder.put(attributesList);
        }

        public OperationResult<UserAttributesListWrapper> delete(){
            builder.addParams(params);
            return builder.delete();
        }

    }

}
