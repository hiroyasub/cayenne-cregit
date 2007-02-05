begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|itest
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|InitialContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|transaction
operator|.
name|TransactionManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|transaction
operator|.
name|TransactionSynchronizationRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|project
operator|.
name|CayenneUserDir
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|Util
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|geronimo
operator|.
name|transaction
operator|.
name|jta11
operator|.
name|GeronimoTransactionManagerJTA11
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|openejb
operator|.
name|client
operator|.
name|LocalInitialContextFactory
import|;
end_import

begin_comment
comment|/**  * A test OpenEJB container object that provides JNDI and JTA environment to the  * integration tests.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|OpenEJBContainer
block|{
specifier|private
specifier|static
name|OpenEJBContainer
name|singletonContainer
decl_stmt|;
specifier|public
specifier|static
name|OpenEJBContainer
name|getContainer
parameter_list|()
block|{
if|if
condition|(
name|singletonContainer
operator|==
literal|null
condition|)
block|{
name|singletonContainer
operator|=
operator|new
name|OpenEJBContainer
argument_list|()
expr_stmt|;
block|}
return|return
name|singletonContainer
return|;
block|}
specifier|private
name|File
name|openEjbHome
decl_stmt|;
specifier|private
name|GeronimoTransactionManagerJTA11
name|txManager
decl_stmt|;
specifier|private
name|OpenEJBContainer
parameter_list|()
block|{
name|setupOpenEJBHome
argument_list|()
expr_stmt|;
name|setupContainerProperties
argument_list|()
expr_stmt|;
try|try
block|{
name|bootstrapContainer
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error bootrapping OpenEJB container"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|TransactionSynchronizationRegistry
name|getTxSyncRegistry
parameter_list|()
block|{
return|return
name|txManager
return|;
block|}
specifier|public
name|TransactionManager
name|getTxManager
parameter_list|()
block|{
return|return
name|txManager
return|;
block|}
specifier|private
name|void
name|setupOpenEJBHome
parameter_list|()
block|{
name|File
name|currentDir
init|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.dir"
argument_list|)
argument_list|)
decl_stmt|;
name|openEjbHome
operator|=
operator|new
name|File
argument_list|(
name|currentDir
argument_list|,
literal|"target"
operator|+
name|File
operator|.
name|separatorChar
operator|+
literal|"openejb-out"
argument_list|)
expr_stmt|;
name|openEjbHome
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|File
name|openEjbConf
init|=
operator|new
name|File
argument_list|(
name|openEjbHome
argument_list|,
literal|"conf"
argument_list|)
decl_stmt|;
name|openEjbConf
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|File
name|openEjbLogs
init|=
operator|new
name|File
argument_list|(
name|openEjbConf
argument_list|,
literal|"logs"
argument_list|)
decl_stmt|;
name|openEjbLogs
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
comment|// to see Cayenne output, copy default logging properties to openejb dir
name|File
name|logConfig
init|=
operator|new
name|File
argument_list|(
name|openEjbConf
argument_list|,
literal|"logging.properties"
argument_list|)
decl_stmt|;
name|File
name|defaultLogConfig
init|=
name|CayenneUserDir
operator|.
name|getInstance
argument_list|()
operator|.
name|resolveFile
argument_list|(
literal|"cayenne-log.properties"
argument_list|)
decl_stmt|;
if|if
condition|(
name|defaultLogConfig
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|Util
operator|.
name|copy
argument_list|(
name|defaultLogConfig
argument_list|,
name|logConfig
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Can't copy logging configuration to "
operator|+
name|logConfig
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|setupContainerProperties
parameter_list|()
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|Context
operator|.
name|INITIAL_CONTEXT_FACTORY
argument_list|,
name|LocalInitialContextFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"openejb.home"
argument_list|,
name|openEjbHome
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|bootstrapContainer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// somehow OpenEJB LocalInitialContextFactory requires 2 IC's to be initilaized to
comment|// fully bootstrap the environment, so do one empty run here, and then use a
comment|// different IC for binding the environment.
operator|new
name|InitialContext
argument_list|()
expr_stmt|;
name|this
operator|.
name|txManager
operator|=
operator|new
name|GeronimoTransactionManagerJTA11
argument_list|()
expr_stmt|;
operator|new
name|InitialContext
argument_list|()
operator|.
name|bind
argument_list|(
literal|"java:comp/TransactionSynchronizationRegistry"
argument_list|,
name|txManager
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

