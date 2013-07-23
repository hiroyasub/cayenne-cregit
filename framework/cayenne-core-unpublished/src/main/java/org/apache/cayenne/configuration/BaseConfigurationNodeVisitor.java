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
name|configuration
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
name|map
operator|.
name|DataMap
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
name|map
operator|.
name|DbAttribute
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
name|map
operator|.
name|DbEntity
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
name|map
operator|.
name|DbRelationship
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
name|map
operator|.
name|Embeddable
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
name|map
operator|.
name|EmbeddableAttribute
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
name|map
operator|.
name|ObjAttribute
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
name|map
operator|.
name|ObjEntity
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
name|map
operator|.
name|ObjRelationship
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
name|map
operator|.
name|Procedure
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
name|map
operator|.
name|ProcedureParameter
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
name|query
operator|.
name|Query
import|;
end_import

begin_comment
comment|/**  * A {@link ConfigurationNodeVisitor} that does nothing, used as a convenience superclass  * for partial visitors. All methods of this visitor throw an  * {@link UnsupportedOperationException}.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|BaseConfigurationNodeVisitor
parameter_list|<
name|T
parameter_list|>
implements|implements
name|ConfigurationNodeVisitor
argument_list|<
name|T
argument_list|>
block|{
specifier|public
name|T
name|visitDataChannelDescriptor
parameter_list|(
name|DataChannelDescriptor
name|channelDescriptor
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not implemented for DataChannelDescriptor"
argument_list|)
throw|;
block|}
specifier|public
name|T
name|visitDataMap
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not implemented for DataMap"
argument_list|)
throw|;
block|}
specifier|public
name|T
name|visitDataNodeDescriptor
parameter_list|(
name|DataNodeDescriptor
name|nodeDescriptor
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not implemented for DataNodeDescriptor"
argument_list|)
throw|;
block|}
specifier|public
name|T
name|visitDbAttribute
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not implemented for DbAttribute"
argument_list|)
throw|;
block|}
specifier|public
name|T
name|visitDbEntity
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not implemented for DbEntity"
argument_list|)
throw|;
block|}
specifier|public
name|T
name|visitDbRelationship
parameter_list|(
name|DbRelationship
name|relationship
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not implemented for DbRelationship"
argument_list|)
throw|;
block|}
specifier|public
name|T
name|visitEmbeddable
parameter_list|(
name|Embeddable
name|embeddable
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not implemented for Embeddable"
argument_list|)
throw|;
block|}
specifier|public
name|T
name|visitEmbeddableAttribute
parameter_list|(
name|EmbeddableAttribute
name|attribute
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not implemented for EmbeddableAttribute"
argument_list|)
throw|;
block|}
specifier|public
name|T
name|visitObjAttribute
parameter_list|(
name|ObjAttribute
name|attribute
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not implemented for ObjAttribute"
argument_list|)
throw|;
block|}
specifier|public
name|T
name|visitObjEntity
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not implemented for ObjEntity"
argument_list|)
throw|;
block|}
specifier|public
name|T
name|visitObjRelationship
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not implemented for ObjRelationship"
argument_list|)
throw|;
block|}
specifier|public
name|T
name|visitProcedure
parameter_list|(
name|Procedure
name|procedure
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not implemented for Procedure"
argument_list|)
throw|;
block|}
specifier|public
name|T
name|visitProcedureParameter
parameter_list|(
name|ProcedureParameter
name|parameter
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not implemented for ProcedureParameter"
argument_list|)
throw|;
block|}
specifier|public
name|T
name|visitQuery
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not implemented for Query"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit
