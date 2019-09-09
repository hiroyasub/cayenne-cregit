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
name|crypto
operator|.
name|map
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
name|CayenneRuntimeException
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
name|DataMapLoader
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
name|log
operator|.
name|JdbcEventLogger
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
name|resource
operator|.
name|Resource
import|;
end_import

begin_comment
comment|/**  * DataMapLoader is overridden to warn and disable optimistic locking  * for encrypted attributes since they don't support optimistic locking.  * The encryption method will return different encrypted values every  * time it is executed, even with the same plain text, so these values   * cannot be compared for locking purposes.  *   * @since 4.2  *  */
end_comment

begin_class
specifier|public
class|class
name|CryptoDataMapLoader
implements|implements
name|DataMapLoader
block|{
specifier|protected
specifier|final
name|DataMapLoader
name|delegate
decl_stmt|;
specifier|protected
specifier|final
name|ColumnMapper
name|columnMapper
decl_stmt|;
specifier|protected
specifier|final
name|JdbcEventLogger
name|jdbcEventLogger
decl_stmt|;
specifier|public
name|CryptoDataMapLoader
parameter_list|(
annotation|@
name|Inject
name|DataMapLoader
name|delegate
parameter_list|,
annotation|@
name|Inject
name|ColumnMapper
name|columnMapper
parameter_list|,
annotation|@
name|Inject
name|JdbcEventLogger
name|jdbcEventLogger
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|columnMapper
operator|=
name|columnMapper
expr_stmt|;
name|this
operator|.
name|jdbcEventLogger
operator|=
name|jdbcEventLogger
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|DataMap
name|load
parameter_list|(
name|Resource
name|configurationResource
parameter_list|)
throws|throws
name|CayenneRuntimeException
block|{
name|DataMap
name|result
init|=
name|delegate
operator|.
name|load
argument_list|(
name|configurationResource
argument_list|)
decl_stmt|;
for|for
control|(
name|ObjEntity
name|entity
range|:
name|result
operator|.
name|getObjEntities
argument_list|()
control|)
block|{
if|if
condition|(
name|entity
operator|.
name|getLockType
argument_list|()
operator|==
name|ObjEntity
operator|.
name|LOCK_TYPE_OPTIMISTIC
condition|)
block|{
for|for
control|(
name|ObjAttribute
name|attr
range|:
name|entity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
if|if
condition|(
name|attr
operator|.
name|isUsedForLocking
argument_list|()
operator|&&
name|attr
operator|.
name|getDbAttribute
argument_list|()
operator|!=
literal|null
operator|&&
name|columnMapper
operator|.
name|isEncrypted
argument_list|(
name|attr
operator|.
name|getDbAttribute
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|attrName
init|=
name|entity
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|attr
operator|.
name|getName
argument_list|()
decl_stmt|;
name|jdbcEventLogger
operator|.
name|log
argument_list|(
literal|"WARN: Encrypted attributes like '"
operator|+
name|attrName
operator|+
literal|"' cannot be used for "
operator|+
literal|"optimistic locking. Locking will be disabled for this attribute."
argument_list|)
expr_stmt|;
name|attr
operator|.
name|setUsedForLocking
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

