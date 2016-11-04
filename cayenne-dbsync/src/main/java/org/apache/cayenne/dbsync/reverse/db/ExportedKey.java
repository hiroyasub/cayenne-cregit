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
name|dbsync
operator|.
name|reverse
operator|.
name|db
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
name|util
operator|.
name|EqualsBuilder
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
name|HashCodeBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|builder
operator|.
name|CompareToBuilder
import|;
end_import

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
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_comment
comment|/**  * A representation of relationship between two tables in database. It can be used for creating names  * for relationships.  *  * @since 4.0  */
end_comment

begin_class
class|class
name|ExportedKey
implements|implements
name|Comparable
block|{
specifier|private
specifier|final
name|String
name|pkCatalog
decl_stmt|;
specifier|private
specifier|final
name|String
name|pkSchema
decl_stmt|;
specifier|private
specifier|final
name|String
name|pkTable
decl_stmt|;
specifier|private
specifier|final
name|String
name|pkColumn
decl_stmt|;
specifier|private
specifier|final
name|String
name|fkCatalog
decl_stmt|;
specifier|private
specifier|final
name|String
name|fkSchema
decl_stmt|;
specifier|private
specifier|final
name|String
name|fkTable
decl_stmt|;
specifier|private
specifier|final
name|String
name|fkColumn
decl_stmt|;
specifier|private
specifier|final
name|String
name|fkName
decl_stmt|;
specifier|private
specifier|final
name|String
name|pkName
decl_stmt|;
specifier|private
specifier|final
name|short
name|keySeq
decl_stmt|;
specifier|public
name|ExportedKey
parameter_list|(
name|String
name|pkTable
parameter_list|,
name|String
name|pkColumn
parameter_list|,
name|String
name|pkName
parameter_list|,
name|String
name|fkTable
parameter_list|,
name|String
name|fkColumn
parameter_list|,
name|String
name|fkName
parameter_list|,
name|short
name|keySeq
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|pkTable
argument_list|,
name|pkColumn
argument_list|,
name|pkName
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|fkTable
argument_list|,
name|fkColumn
argument_list|,
name|fkName
argument_list|,
name|keySeq
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ExportedKey
parameter_list|(
name|String
name|pkCatalog
parameter_list|,
name|String
name|pkSchema
parameter_list|,
name|String
name|pkTable
parameter_list|,
name|String
name|pkColumn
parameter_list|,
name|String
name|pkName
parameter_list|,
name|String
name|fkCatalog
parameter_list|,
name|String
name|fkSchema
parameter_list|,
name|String
name|fkTable
parameter_list|,
name|String
name|fkColumn
parameter_list|,
name|String
name|fkName
parameter_list|,
name|short
name|keySeq
parameter_list|)
block|{
name|this
operator|.
name|pkCatalog
operator|=
name|pkCatalog
expr_stmt|;
name|this
operator|.
name|pkSchema
operator|=
name|pkSchema
expr_stmt|;
name|this
operator|.
name|pkTable
operator|=
name|pkTable
expr_stmt|;
name|this
operator|.
name|pkColumn
operator|=
name|pkColumn
expr_stmt|;
name|this
operator|.
name|pkName
operator|=
name|pkName
expr_stmt|;
name|this
operator|.
name|fkCatalog
operator|=
name|fkCatalog
expr_stmt|;
name|this
operator|.
name|fkSchema
operator|=
name|fkSchema
expr_stmt|;
name|this
operator|.
name|fkTable
operator|=
name|fkTable
expr_stmt|;
name|this
operator|.
name|fkColumn
operator|=
name|fkColumn
expr_stmt|;
name|this
operator|.
name|fkName
operator|=
name|fkName
expr_stmt|;
name|this
operator|.
name|keySeq
operator|=
name|keySeq
expr_stmt|;
block|}
comment|/**      * Extracts data from a resultset pointing to a exported key to      * ExportedKey class instance      *      * @param rs ResultSet pointing to a exported key, fetched using      *           DataBaseMetaData.getExportedKeys(...)      */
specifier|public
specifier|static
name|ExportedKey
name|extractData
parameter_list|(
name|ResultSet
name|rs
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
operator|new
name|ExportedKey
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"PKTABLE_CAT"
argument_list|)
argument_list|,
name|rs
operator|.
name|getString
argument_list|(
literal|"PKTABLE_SCHEM"
argument_list|)
argument_list|,
name|rs
operator|.
name|getString
argument_list|(
literal|"PKTABLE_NAME"
argument_list|)
argument_list|,
name|rs
operator|.
name|getString
argument_list|(
literal|"PKCOLUMN_NAME"
argument_list|)
argument_list|,
name|rs
operator|.
name|getString
argument_list|(
literal|"PK_NAME"
argument_list|)
argument_list|,
name|rs
operator|.
name|getString
argument_list|(
literal|"FKTABLE_CAT"
argument_list|)
argument_list|,
name|rs
operator|.
name|getString
argument_list|(
literal|"FKTABLE_SCHEM"
argument_list|)
argument_list|,
name|rs
operator|.
name|getString
argument_list|(
literal|"FKTABLE_NAME"
argument_list|)
argument_list|,
name|rs
operator|.
name|getString
argument_list|(
literal|"FKCOLUMN_NAME"
argument_list|)
argument_list|,
name|rs
operator|.
name|getString
argument_list|(
literal|"FK_NAME"
argument_list|)
argument_list|,
name|rs
operator|.
name|getShort
argument_list|(
literal|"KEY_SEQ"
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|String
name|getPkCatalog
parameter_list|()
block|{
return|return
name|pkCatalog
return|;
block|}
specifier|public
name|String
name|getPkSchema
parameter_list|()
block|{
return|return
name|pkSchema
return|;
block|}
specifier|public
name|String
name|getFkCatalog
parameter_list|()
block|{
return|return
name|fkCatalog
return|;
block|}
specifier|public
name|String
name|getFkSchema
parameter_list|()
block|{
return|return
name|fkSchema
return|;
block|}
comment|/**      * @return source table name      */
specifier|public
name|String
name|getPKTableName
parameter_list|()
block|{
return|return
name|pkTable
return|;
block|}
comment|/**      * @return destination table name      */
specifier|public
name|String
name|getFKTableName
parameter_list|()
block|{
return|return
name|fkTable
return|;
block|}
comment|/**      * @return source column name      */
specifier|public
name|String
name|getPKColumnName
parameter_list|()
block|{
return|return
name|pkColumn
return|;
block|}
comment|/**      * @return destination column name      */
specifier|public
name|String
name|getFKColumnName
parameter_list|()
block|{
return|return
name|fkColumn
return|;
block|}
comment|/**      * @return PK name      */
specifier|public
name|String
name|getPKName
parameter_list|()
block|{
return|return
name|pkName
return|;
block|}
comment|/**      * @return FK name      */
specifier|public
name|String
name|getFKName
parameter_list|()
block|{
return|return
name|fkName
return|;
block|}
specifier|public
name|short
name|getKeySeq
parameter_list|()
block|{
return|return
name|keySeq
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|obj
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|obj
operator|.
name|getClass
argument_list|()
operator|!=
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ExportedKey
name|rhs
init|=
operator|(
name|ExportedKey
operator|)
name|obj
decl_stmt|;
return|return
operator|new
name|EqualsBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|this
operator|.
name|pkCatalog
argument_list|,
name|rhs
operator|.
name|pkCatalog
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|pkSchema
argument_list|,
name|rhs
operator|.
name|pkSchema
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|pkTable
argument_list|,
name|rhs
operator|.
name|pkTable
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|pkColumn
argument_list|,
name|rhs
operator|.
name|pkColumn
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|fkCatalog
argument_list|,
name|rhs
operator|.
name|fkCatalog
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|fkSchema
argument_list|,
name|rhs
operator|.
name|fkSchema
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|fkTable
argument_list|,
name|rhs
operator|.
name|fkTable
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|fkColumn
argument_list|,
name|rhs
operator|.
name|fkColumn
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|fkName
argument_list|,
name|rhs
operator|.
name|fkName
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|pkName
argument_list|,
name|rhs
operator|.
name|pkName
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|keySeq
argument_list|,
name|rhs
operator|.
name|keySeq
argument_list|)
operator|.
name|isEquals
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
operator|new
name|HashCodeBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|pkCatalog
argument_list|)
operator|.
name|append
argument_list|(
name|pkSchema
argument_list|)
operator|.
name|append
argument_list|(
name|pkTable
argument_list|)
operator|.
name|append
argument_list|(
name|pkColumn
argument_list|)
operator|.
name|append
argument_list|(
name|fkCatalog
argument_list|)
operator|.
name|append
argument_list|(
name|fkSchema
argument_list|)
operator|.
name|append
argument_list|(
name|fkTable
argument_list|)
operator|.
name|append
argument_list|(
name|fkColumn
argument_list|)
operator|.
name|append
argument_list|(
name|fkName
argument_list|)
operator|.
name|append
argument_list|(
name|pkName
argument_list|)
operator|.
name|append
argument_list|(
name|keySeq
argument_list|)
operator|.
name|toHashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|compareTo
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|==
literal|null
operator|||
operator|!
name|obj
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
if|if
condition|(
name|obj
operator|==
name|this
condition|)
block|{
return|return
literal|0
return|;
block|}
name|ExportedKey
name|rhs
init|=
operator|(
name|ExportedKey
operator|)
name|obj
decl_stmt|;
return|return
operator|new
name|CompareToBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|pkCatalog
argument_list|,
name|rhs
operator|.
name|pkCatalog
argument_list|)
operator|.
name|append
argument_list|(
name|pkSchema
argument_list|,
name|rhs
operator|.
name|pkSchema
argument_list|)
operator|.
name|append
argument_list|(
name|pkTable
argument_list|,
name|rhs
operator|.
name|pkTable
argument_list|)
operator|.
name|append
argument_list|(
name|pkName
argument_list|,
name|rhs
operator|.
name|pkName
argument_list|)
operator|.
name|append
argument_list|(
name|fkCatalog
argument_list|,
name|rhs
operator|.
name|fkCatalog
argument_list|)
operator|.
name|append
argument_list|(
name|fkSchema
argument_list|,
name|rhs
operator|.
name|fkSchema
argument_list|)
operator|.
name|append
argument_list|(
name|fkTable
argument_list|,
name|rhs
operator|.
name|fkTable
argument_list|)
operator|.
name|append
argument_list|(
name|fkName
argument_list|,
name|rhs
operator|.
name|fkName
argument_list|)
operator|.
name|append
argument_list|(
name|keySeq
argument_list|,
name|rhs
operator|.
name|keySeq
argument_list|)
operator|.
name|append
argument_list|(
name|pkColumn
argument_list|,
name|rhs
operator|.
name|pkColumn
argument_list|)
operator|.
name|append
argument_list|(
name|fkColumn
argument_list|,
name|rhs
operator|.
name|fkColumn
argument_list|)
operator|.
name|toComparison
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getStrKey
argument_list|()
operator|+
literal|" # "
operator|+
name|keySeq
return|;
block|}
specifier|public
name|String
name|getStrKey
parameter_list|()
block|{
return|return
name|pkCatalog
operator|+
literal|"."
operator|+
name|pkSchema
operator|+
literal|"."
operator|+
name|pkTable
operator|+
literal|"."
operator|+
name|pkColumn
operator|+
literal|"<- "
operator|+
name|fkCatalog
operator|+
literal|"."
operator|+
name|fkSchema
operator|+
literal|"."
operator|+
name|fkTable
operator|+
literal|"."
operator|+
name|fkColumn
return|;
block|}
block|}
end_class

end_unit

