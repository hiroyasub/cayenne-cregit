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
name|dbsync
operator|.
name|reverse
operator|.
name|dbload
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
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
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
name|util
operator|.
name|CompareToBuilder
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
name|cayenne
operator|.
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  * A representation of relationship between two tables in database. It can be used for creating names  * for relationships.  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|ExportedKey
implements|implements
name|Comparable
argument_list|<
name|ExportedKey
argument_list|>
block|{
specifier|private
specifier|final
name|KeyData
name|pk
decl_stmt|;
specifier|private
specifier|final
name|KeyData
name|fk
decl_stmt|;
specifier|private
specifier|final
name|short
name|keySeq
decl_stmt|;
comment|/**      * Extracts data from a resultset pointing to a exported key to      * ExportedKey class instance      *      * @param rs ResultSet pointing to a exported key, fetched using      *           DataBaseMetaData.getExportedKeys(...)      */
name|ExportedKey
parameter_list|(
name|ResultSet
name|rs
parameter_list|)
throws|throws
name|SQLException
block|{
name|String
name|pkCatalog
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"PKTABLE_CAT"
argument_list|)
decl_stmt|;
name|String
name|pkSchema
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"PKTABLE_SCHEM"
argument_list|)
decl_stmt|;
name|String
name|pkTable
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"PKTABLE_NAME"
argument_list|)
decl_stmt|;
name|String
name|pkColumn
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"PKCOLUMN_NAME"
argument_list|)
decl_stmt|;
name|String
name|pkName
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"PK_NAME"
argument_list|)
decl_stmt|;
name|pk
operator|=
operator|new
name|KeyData
argument_list|(
name|pkCatalog
argument_list|,
name|pkSchema
argument_list|,
name|pkTable
argument_list|,
name|pkColumn
argument_list|,
name|pkName
argument_list|)
expr_stmt|;
name|String
name|fkCatalog
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"FKTABLE_CAT"
argument_list|)
decl_stmt|;
name|String
name|fkSchema
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"FKTABLE_SCHEM"
argument_list|)
decl_stmt|;
name|String
name|fkTable
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"FKTABLE_NAME"
argument_list|)
decl_stmt|;
name|String
name|fkColumn
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"FKCOLUMN_NAME"
argument_list|)
decl_stmt|;
name|String
name|fkName
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"FK_NAME"
argument_list|)
decl_stmt|;
name|fk
operator|=
operator|new
name|KeyData
argument_list|(
name|fkCatalog
argument_list|,
name|fkSchema
argument_list|,
name|fkTable
argument_list|,
name|fkColumn
argument_list|,
name|fkName
argument_list|)
expr_stmt|;
name|keySeq
operator|=
name|rs
operator|.
name|getShort
argument_list|(
literal|"KEY_SEQ"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|KeyData
name|getPk
parameter_list|()
block|{
return|return
name|pk
return|;
block|}
specifier|public
name|KeyData
name|getFk
parameter_list|()
block|{
return|return
name|fk
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
name|pk
argument_list|,
name|rhs
operator|.
name|pk
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|fk
argument_list|,
name|rhs
operator|.
name|fk
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
name|pk
argument_list|)
operator|.
name|append
argument_list|(
name|fk
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
name|ExportedKey
name|rhs
parameter_list|)
block|{
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|rhs
argument_list|)
expr_stmt|;
if|if
condition|(
name|rhs
operator|==
name|this
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
operator|new
name|CompareToBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|pk
argument_list|,
name|rhs
operator|.
name|pk
argument_list|)
operator|.
name|append
argument_list|(
name|fk
argument_list|,
name|rhs
operator|.
name|fk
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
name|String
name|getStrKey
parameter_list|()
block|{
return|return
name|pk
operator|+
literal|"<- "
operator|+
name|fk
return|;
block|}
specifier|public
specifier|static
class|class
name|KeyData
implements|implements
name|Comparable
argument_list|<
name|KeyData
argument_list|>
block|{
specifier|private
specifier|final
name|String
name|catalog
decl_stmt|;
specifier|private
specifier|final
name|String
name|schema
decl_stmt|;
specifier|private
specifier|final
name|String
name|table
decl_stmt|;
specifier|private
specifier|final
name|String
name|column
decl_stmt|;
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
name|KeyData
parameter_list|(
name|String
name|catalog
parameter_list|,
name|String
name|schema
parameter_list|,
name|String
name|table
parameter_list|,
name|String
name|column
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|catalog
operator|=
name|catalog
expr_stmt|;
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
name|this
operator|.
name|table
operator|=
name|table
expr_stmt|;
name|this
operator|.
name|column
operator|=
name|column
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getCatalog
parameter_list|()
block|{
return|return
name|catalog
return|;
block|}
specifier|public
name|String
name|getSchema
parameter_list|()
block|{
return|return
name|schema
return|;
block|}
specifier|public
name|String
name|getTable
parameter_list|()
block|{
return|return
name|table
return|;
block|}
specifier|public
name|String
name|getColumn
parameter_list|()
block|{
return|return
name|column
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
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
name|catalog
operator|+
literal|"."
operator|+
name|schema
operator|+
literal|"."
operator|+
name|table
operator|+
literal|"."
operator|+
name|column
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|compareTo
parameter_list|(
name|KeyData
name|rhs
parameter_list|)
block|{
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|rhs
argument_list|)
expr_stmt|;
if|if
condition|(
name|rhs
operator|==
name|this
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
operator|new
name|CompareToBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|catalog
argument_list|,
name|rhs
operator|.
name|catalog
argument_list|)
operator|.
name|append
argument_list|(
name|schema
argument_list|,
name|rhs
operator|.
name|schema
argument_list|)
operator|.
name|append
argument_list|(
name|table
argument_list|,
name|rhs
operator|.
name|table
argument_list|)
operator|.
name|append
argument_list|(
name|column
argument_list|,
name|rhs
operator|.
name|column
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|,
name|rhs
operator|.
name|name
argument_list|)
operator|.
name|toComparison
argument_list|()
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
name|KeyData
name|rhs
init|=
operator|(
name|KeyData
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
name|catalog
argument_list|,
name|rhs
operator|.
name|catalog
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|schema
argument_list|,
name|rhs
operator|.
name|schema
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|table
argument_list|,
name|rhs
operator|.
name|table
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|column
argument_list|,
name|rhs
operator|.
name|column
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|name
argument_list|,
name|rhs
operator|.
name|name
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
name|catalog
argument_list|)
operator|.
name|append
argument_list|(
name|schema
argument_list|)
operator|.
name|append
argument_list|(
name|table
argument_list|)
operator|.
name|append
argument_list|(
name|column
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|)
operator|.
name|toHashCode
argument_list|()
return|;
block|}
comment|/**          * Validate that entity is for this key (exists and has same catalog/schema)          * @param entity to validate          * @return is entity matches for this key          */
specifier|public
name|boolean
name|validateEntity
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
if|if
condition|(
name|entity
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
name|Util
operator|.
name|isEmptyString
argument_list|(
name|catalog
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|Util
operator|.
name|isEmptyString
argument_list|(
name|entity
operator|.
name|getCatalog
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
if|if
condition|(
operator|!
name|catalog
operator|.
name|equals
argument_list|(
name|entity
operator|.
name|getCatalog
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|schema
argument_list|)
condition|)
block|{
return|return
name|Util
operator|.
name|isEmptyString
argument_list|(
name|entity
operator|.
name|getSchema
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|schema
operator|.
name|equals
argument_list|(
name|entity
operator|.
name|getSchema
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

