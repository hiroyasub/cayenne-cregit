begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|//package org.apache.cayenne.rop.server;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//import org.apache.cayenne.configuration.server.CayenneServerModuleProvider;
end_comment

begin_comment
comment|//import org.apache.cayenne.configuration.server.ServerModule;
end_comment

begin_comment
comment|//import org.apache.cayenne.di.Module;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//import java.util.Collection;
end_comment

begin_comment
comment|//import java.util.Collections;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|///**
end_comment

begin_comment
comment|// * Created by Arseni on 01.11.17.
end_comment

begin_comment
comment|// */
end_comment

begin_comment
comment|//public class ROPServerModuleProvider implements CayenneServerModuleProvider {
end_comment

begin_comment
comment|//    @Override
end_comment

begin_comment
comment|//    public Module module() {
end_comment

begin_comment
comment|//        return new ROPServerModule();
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    @Override
end_comment

begin_comment
comment|//    public Class<? extends Module> moduleType() {
end_comment

begin_comment
comment|//        return ROPServerModule.class;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    @Override
end_comment

begin_comment
comment|//    public Collection<Class<? extends Module>> overrides() {
end_comment

begin_comment
comment|//        Collection modules = Collections.singletonList(ServerModule.class);
end_comment

begin_comment
comment|//        return modules;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//}
end_comment

end_unit

