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

begin_comment
comment|/**  * A default implementation of {@link DataChannelDescriptorMerger}. The general rule of  * merge is that the order of descriptors on the merge list matters. If there are two  * conflicting metadata objects belonging to two descriptors, an object from the last  * descriptor takes precedence over the object from the first one. This way it is easy to  * override pieces of metadata. This is also similar to how DI modules are merged in  * Cayenne. So this is how the merge works:  *<ul>  *<li>Merged descriptor name is the same as the name of the last descriptor on the merge  * list.</li>  *<li>Merged descriptor properties are the same as the properties of the last descriptor  * on the merge list. I.e. properties are not merged to avoid invalid combinations and  * unexpected runtime behavior.</li>  *<li>If there are two or more DataMaps with the same name, only one DataMap is placed in  * the merged descriptor, the rest are discarded. DataMap with highest index in the  * descriptor array is chosen per precedence rule above.</li>  *<li>If there are two or more DataNodes with the same name, only one DataNodes is placed  * in the merged descriptor, the rest are discarded. DataNodes with highest index in the  * descriptor array is chosen per precedence rule above.</li>  *</ul>  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|DefaultDataChannelDescriptorMerger
implements|implements
name|DataChannelDescriptorMerger
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
name|DefaultDataChannelDescriptorMerger
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|DataChannelDescriptor
name|merge
parameter_list|(
name|DataChannelDescriptor
modifier|...
name|descriptors
parameter_list|)
block|{
if|if
condition|(
name|descriptors
operator|==
literal|null
operator|||
name|descriptors
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null or empty descriptors"
argument_list|)
throw|;
block|}
if|if
condition|(
name|descriptors
operator|.
name|length
operator|==
literal|1
condition|)
block|{
return|return
name|descriptors
index|[
literal|0
index|]
return|;
block|}
name|int
name|len
init|=
name|descriptors
operator|.
name|length
decl_stmt|;
comment|// merge into a new descriptor; do not alter source descriptors
name|DataChannelDescriptor
name|merged
init|=
operator|new
name|DataChannelDescriptor
argument_list|()
decl_stmt|;
name|merged
operator|.
name|setName
argument_list|(
name|descriptors
index|[
name|len
operator|-
literal|1
index|]
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|merged
operator|.
name|getProperties
argument_list|()
operator|.
name|putAll
argument_list|(
name|descriptors
index|[
name|len
operator|-
literal|1
index|]
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
comment|// iterate in reverse order to reduce add/remove operations
for|for
control|(
name|int
name|i
init|=
name|len
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|DataChannelDescriptor
name|descriptor
init|=
name|descriptors
index|[
name|i
index|]
decl_stmt|;
comment|// DataMaps are merged by reference, as we don't change them
comment|// TODO: they still have a link to the unmerged descriptor, is it bad?
for|for
control|(
name|DataMap
name|map
range|:
name|descriptor
operator|.
name|getDataMaps
argument_list|()
control|)
block|{
comment|// report conflicting DataMap and leave the existing copy
name|DataMap
name|existing
init|=
name|merged
operator|.
name|getDataMap
argument_list|(
name|map
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|existing
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Discarding overridden DataMap '"
operator|+
name|map
operator|.
name|getName
argument_list|()
operator|+
literal|"' from descriptor '"
operator|+
name|descriptor
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Using DataMap '"
operator|+
name|map
operator|.
name|getName
argument_list|()
operator|+
literal|"' from descriptor '"
operator|+
name|descriptor
operator|.
name|getName
argument_list|()
operator|+
literal|"' in merged descriptor"
argument_list|)
expr_stmt|;
name|merged
operator|.
name|getDataMaps
argument_list|()
operator|.
name|add
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
block|}
comment|// DataNodes are merged by copy as we may modify them (changing map linking)
for|for
control|(
name|DataNodeDescriptor
name|node
range|:
name|descriptor
operator|.
name|getNodeDescriptors
argument_list|()
control|)
block|{
name|DataNodeDescriptor
name|existing
init|=
name|merged
operator|.
name|getNodeDescriptor
argument_list|(
name|node
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|existing
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Discarding overridden DataNode '"
operator|+
name|node
operator|.
name|getName
argument_list|()
operator|+
literal|"' from descriptor '"
operator|+
name|descriptor
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|mapName
range|:
name|node
operator|.
name|getDataMapNames
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|existing
operator|.
name|getDataMapNames
argument_list|()
operator|.
name|contains
argument_list|(
name|mapName
argument_list|)
condition|)
block|{
name|existing
operator|.
name|getDataMapNames
argument_list|()
operator|.
name|add
argument_list|(
name|mapName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Using DataNode '"
operator|+
name|node
operator|.
name|getName
argument_list|()
operator|+
literal|"' from descriptor '"
operator|+
name|descriptor
operator|.
name|getName
argument_list|()
operator|+
literal|"' in merged descriptor"
argument_list|)
expr_stmt|;
name|merged
operator|.
name|getNodeDescriptors
argument_list|()
operator|.
name|add
argument_list|(
name|cloneDataNodeDescriptor
argument_list|(
name|node
argument_list|,
name|merged
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|merged
return|;
block|}
specifier|protected
name|DataNodeDescriptor
name|cloneDataNodeDescriptor
parameter_list|(
name|DataNodeDescriptor
name|original
parameter_list|,
name|DataChannelDescriptor
name|targetOwner
parameter_list|)
block|{
name|DataNodeDescriptor
name|clone
init|=
operator|new
name|DataNodeDescriptor
argument_list|(
name|original
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|// do not clone 'configurationSource' as we may change the structure of the node
name|clone
operator|.
name|setAdapterType
argument_list|(
name|original
operator|.
name|getAdapterType
argument_list|()
argument_list|)
expr_stmt|;
name|clone
operator|.
name|setDataChannelDescriptor
argument_list|(
name|targetOwner
argument_list|)
expr_stmt|;
name|clone
operator|.
name|setDataSourceDescriptor
argument_list|(
name|original
operator|.
name|getDataSourceDescriptor
argument_list|()
argument_list|)
expr_stmt|;
name|clone
operator|.
name|setDataSourceFactoryType
argument_list|(
name|original
operator|.
name|getDataSourceFactoryType
argument_list|()
argument_list|)
expr_stmt|;
name|clone
operator|.
name|setParameters
argument_list|(
name|original
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
name|clone
operator|.
name|setSchemaUpdateStrategyType
argument_list|(
name|original
operator|.
name|getSchemaUpdateStrategyType
argument_list|()
argument_list|)
expr_stmt|;
name|clone
operator|.
name|getDataMapNames
argument_list|()
operator|.
name|addAll
argument_list|(
name|original
operator|.
name|getDataMapNames
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|clone
return|;
block|}
block|}
end_class

end_unit

