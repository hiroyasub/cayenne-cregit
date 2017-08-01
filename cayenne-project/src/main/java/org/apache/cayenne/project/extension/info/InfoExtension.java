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
name|project
operator|.
name|extension
operator|.
name|info
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|ConfigurationNodeVisitor
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
name|configuration
operator|.
name|xml
operator|.
name|DataChannelMetaData
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
name|di
operator|.
name|Inject
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
name|Project
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
name|extension
operator|.
name|BaseNamingDelegate
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
name|extension
operator|.
name|LoaderDelegate
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
name|extension
operator|.
name|ProjectExtension
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
name|extension
operator|.
name|SaverDelegate
import|;
end_import

begin_comment
comment|/**  * Extension that provides additional properties for project entities.  * It stores data in {@link ObjectInfo} associated with objects via {@link DataChannelMetaData}.  * Currently used by Modeler and cgen tools to provide user comments.  *  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|InfoExtension
implements|implements
name|ProjectExtension
block|{
specifier|static
specifier|final
name|String
name|NAMESPACE
init|=
literal|"http://cayenne.apache.org/schema/"
operator|+
name|Project
operator|.
name|VERSION
operator|+
literal|"/info"
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataChannelMetaData
name|metaData
decl_stmt|;
annotation|@
name|Override
specifier|public
name|LoaderDelegate
name|createLoaderDelegate
parameter_list|()
block|{
return|return
operator|new
name|InfoLoaderDelegate
argument_list|(
name|metaData
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|SaverDelegate
name|createSaverDelegate
parameter_list|()
block|{
return|return
operator|new
name|InfoSaverDelegate
argument_list|(
name|metaData
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ConfigurationNodeVisitor
argument_list|<
name|String
argument_list|>
name|createNamingDelegate
parameter_list|()
block|{
return|return
operator|new
name|BaseNamingDelegate
argument_list|()
return|;
block|}
block|}
end_class

end_unit

