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
name|access
operator|.
name|jdbc
operator|.
name|reader
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
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
name|DataRow
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
name|access
operator|.
name|jdbc
operator|.
name|RowDescriptor
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
name|EntityInheritanceTree
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
name|query
operator|.
name|EntityResultSegment
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
class|class
name|InheritanceAwareEntityRowReader
extends|extends
name|EntityRowReader
block|{
specifier|private
name|EntityInheritanceTree
name|entityInheritanceTree
decl_stmt|;
specifier|public
name|InheritanceAwareEntityRowReader
parameter_list|(
name|RowDescriptor
name|descriptor
parameter_list|,
name|EntityResultSegment
name|segmentMetadata
parameter_list|,
name|DataRowPostProcessor
name|postProcessor
parameter_list|)
block|{
name|super
argument_list|(
name|descriptor
argument_list|,
name|segmentMetadata
argument_list|,
name|postProcessor
argument_list|)
expr_stmt|;
name|this
operator|.
name|entityInheritanceTree
operator|=
name|segmentMetadata
operator|.
name|getClassDescriptor
argument_list|()
operator|.
name|getEntityInheritanceTree
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
name|void
name|postprocessRow
parameter_list|(
name|ResultSet
name|resultSet
parameter_list|,
name|DataRow
name|dataRow
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|postProcessor
operator|!=
literal|null
condition|)
block|{
name|postProcessor
operator|.
name|postprocessRow
argument_list|(
name|resultSet
argument_list|,
name|dataRow
argument_list|)
expr_stmt|;
block|}
name|ObjEntity
name|entity
init|=
name|entityInheritanceTree
operator|.
name|entityMatchingRow
argument_list|(
name|dataRow
argument_list|)
decl_stmt|;
name|dataRow
operator|.
name|setEntityName
argument_list|(
name|entity
operator|!=
literal|null
condition|?
name|entity
operator|.
name|getName
argument_list|()
else|:
name|entityName
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

