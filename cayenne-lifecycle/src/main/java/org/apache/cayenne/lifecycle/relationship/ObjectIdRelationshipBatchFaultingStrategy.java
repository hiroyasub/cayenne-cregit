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
name|lifecycle
operator|.
name|relationship
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
name|DataObject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * A faulting strategy that does batch-faulting of related objects whenever a first  * ObjectId relationship is accessed.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|ObjectIdRelationshipBatchFaultingStrategy
implements|implements
name|ObjectIdRelationshipFaultingStrategy
block|{
specifier|private
name|ThreadLocal
argument_list|<
name|List
argument_list|<
name|ObjectIdBatchSourceItem
argument_list|>
argument_list|>
name|batchSources
decl_stmt|;
specifier|public
name|ObjectIdRelationshipBatchFaultingStrategy
parameter_list|()
block|{
name|this
operator|.
name|batchSources
operator|=
operator|new
name|ThreadLocal
argument_list|<>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|afterObjectLoaded
parameter_list|(
name|DataObject
name|object
parameter_list|)
block|{
name|String
name|uuidProperty
init|=
name|objectIdPropertyName
argument_list|(
name|object
argument_list|)
decl_stmt|;
name|String
name|uuidRelationship
init|=
name|objectIdRelationshipName
argument_list|(
name|uuidProperty
argument_list|)
decl_stmt|;
name|String
name|uuid
init|=
operator|(
name|String
operator|)
name|object
operator|.
name|readProperty
argument_list|(
name|uuidProperty
argument_list|)
decl_stmt|;
if|if
condition|(
name|uuid
operator|==
literal|null
condition|)
block|{
name|object
operator|.
name|writePropertyDirectly
argument_list|(
name|uuidRelationship
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|List
argument_list|<
name|ObjectIdBatchSourceItem
argument_list|>
name|sources
init|=
name|batchSources
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|sources
operator|==
literal|null
condition|)
block|{
name|sources
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|batchSources
operator|.
name|set
argument_list|(
name|sources
argument_list|)
expr_stmt|;
block|}
name|sources
operator|.
name|add
argument_list|(
operator|new
name|ObjectIdBatchSourceItem
argument_list|(
name|object
argument_list|,
name|uuid
argument_list|,
name|uuidRelationship
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|afterQuery
parameter_list|()
block|{
name|List
argument_list|<
name|ObjectIdBatchSourceItem
argument_list|>
name|sources
init|=
name|batchSources
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|sources
operator|!=
literal|null
condition|)
block|{
name|batchSources
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|ObjectIdBatchFault
name|batchFault
init|=
operator|new
name|ObjectIdBatchFault
argument_list|(
name|sources
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getObject
argument_list|()
operator|.
name|getObjectContext
argument_list|()
argument_list|,
name|sources
argument_list|)
decl_stmt|;
for|for
control|(
name|ObjectIdBatchSourceItem
name|source
range|:
name|sources
control|)
block|{
name|source
operator|.
name|getObject
argument_list|()
operator|.
name|writePropertyDirectly
argument_list|(
name|source
operator|.
name|getObjectIdRelationship
argument_list|()
argument_list|,
operator|new
name|ObjectIdFault
argument_list|(
name|batchFault
argument_list|,
name|source
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|String
name|objectIdRelationshipName
parameter_list|(
name|String
name|uuidPropertyName
parameter_list|)
block|{
return|return
literal|"cay:related:"
operator|+
name|uuidPropertyName
return|;
block|}
name|String
name|objectIdPropertyName
parameter_list|(
name|DataObject
name|object
parameter_list|)
block|{
name|ObjectIdRelationship
name|annotation
init|=
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|ObjectIdRelationship
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|annotation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Object class is not annotated with @UuidRelationship: "
operator|+
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
comment|// TODO: I guess we'll need to cache this metadata for performance if we are to
comment|// support inheritance lookups, etc.
return|return
name|annotation
operator|.
name|value
argument_list|()
return|;
block|}
block|}
end_class

end_unit

