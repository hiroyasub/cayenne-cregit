begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|unit
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|spi
operator|.
name|NamingManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|mock
operator|.
name|jndi
operator|.
name|SimpleNamingContextBuilder
import|;
end_import

begin_comment
comment|/**  * A helper class to setup a shared test JNDI environment.  */
end_comment

begin_class
specifier|public
class|class
name|JNDISetup
block|{
specifier|private
specifier|static
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JNDISetup
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|volatile
name|boolean
name|setup
decl_stmt|;
specifier|public
specifier|static
name|void
name|doSetup
parameter_list|()
block|{
if|if
condition|(
operator|!
name|setup
condition|)
block|{
synchronized|synchronized
init|(
name|JNDISetup
operator|.
name|class
init|)
block|{
if|if
condition|(
operator|!
name|setup
condition|)
block|{
try|try
block|{
name|NamingManager
operator|.
name|setInitialContextFactoryBuilder
argument_list|(
operator|new
name|SimpleNamingContextBuilder
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NamingException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Can't perform JNDI setup, ignoring..."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|setup
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

