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
name|access
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|DataChannel
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
name|ObjectContext
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
name|ObjectContextFactory
import|;
end_import

begin_comment
comment|/**  * A default implementation of the {@link ObjectContextFactory} that builds contexts based  * on the mapped DataDomain configuration.  *   * @author Andrus Adamchik  * @since 3.0  */
end_comment

begin_class
class|class
name|DefaultObjectContextFactory
implements|implements
name|ObjectContextFactory
block|{
specifier|protected
name|DataDomain
name|domain
decl_stmt|;
name|DefaultObjectContextFactory
parameter_list|(
name|DataDomain
name|domain
parameter_list|)
block|{
name|this
operator|.
name|domain
operator|=
name|domain
expr_stmt|;
block|}
specifier|public
name|ObjectContext
name|createObjectContext
parameter_list|(
name|Map
name|properties
parameter_list|)
block|{
return|return
name|createObjectContext
argument_list|(
name|domain
argument_list|,
name|properties
argument_list|)
return|;
block|}
specifier|public
name|ObjectContext
name|createObjectContext
parameter_list|(
name|DataChannel
name|parent
parameter_list|,
name|Map
name|properties
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

