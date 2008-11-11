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
name|map
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|CayenneMapEntry
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
name|XMLEncoder
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
name|XMLSerializable
import|;
end_import

begin_comment
comment|/**  * DbKeyGenerator is an abstraction of a primary key generator It configures the primary  * key generation per DbEntity in a RDBMS independent manner. DbAdapter generates actual  * key values based on the configuration. For more details see data-map.dtd  *   */
end_comment

begin_class
specifier|public
class|class
name|DbKeyGenerator
implements|implements
name|CayenneMapEntry
implements|,
name|XMLSerializable
implements|,
name|Serializable
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ORACLE_TYPE
init|=
literal|"ORACLE"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NAMED_SEQUENCE_TABLE_TYPE
init|=
literal|"NAMED_SEQUENCE_TABLE"
decl_stmt|;
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|DbEntity
name|dbEntity
decl_stmt|;
specifier|protected
name|String
name|generatorType
decl_stmt|;
specifier|protected
name|Integer
name|keyCacheSize
decl_stmt|;
specifier|protected
name|String
name|generatorName
decl_stmt|;
specifier|public
name|DbKeyGenerator
parameter_list|()
block|{
block|}
specifier|public
name|DbKeyGenerator
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
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
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|Object
name|getParent
parameter_list|()
block|{
return|return
name|getDbEntity
argument_list|()
return|;
block|}
specifier|public
name|void
name|setParent
parameter_list|(
name|Object
name|parent
parameter_list|)
block|{
if|if
condition|(
name|parent
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|parent
operator|instanceof
name|DbEntity
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Expected null or DbEntity, got: "
operator|+
name|parent
argument_list|)
throw|;
block|}
name|setDbEntity
argument_list|(
operator|(
name|DbEntity
operator|)
name|parent
argument_list|)
expr_stmt|;
block|}
comment|/**      * Prints itself as XML to the provided XMLEncoder.      *       * @since 1.1      */
specifier|public
name|void
name|encodeAsXML
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|)
block|{
if|if
condition|(
name|getGeneratorType
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|encoder
operator|.
name|println
argument_list|(
literal|"<db-key-generator>"
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|indent
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
literal|"<db-generator-type>"
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|getGeneratorType
argument_list|()
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|"</db-generator-type>"
argument_list|)
expr_stmt|;
if|if
condition|(
name|getGeneratorName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"<db-generator-name>"
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|getGeneratorName
argument_list|()
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|"</db-generator-name>"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getKeyCacheSize
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"<db-key-cache-size>"
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|getKeyCacheSize
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|"</db-key-cache-size>"
argument_list|)
expr_stmt|;
block|}
name|encoder
operator|.
name|indent
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|"</db-key-generator>"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DbEntity
name|getDbEntity
parameter_list|()
block|{
return|return
name|dbEntity
return|;
block|}
specifier|public
name|void
name|setDbEntity
parameter_list|(
name|DbEntity
name|dbEntity
parameter_list|)
block|{
name|this
operator|.
name|dbEntity
operator|=
name|dbEntity
expr_stmt|;
block|}
specifier|public
name|void
name|setGeneratorType
parameter_list|(
name|String
name|generatorType
parameter_list|)
block|{
name|this
operator|.
name|generatorType
operator|=
name|generatorType
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|generatorType
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|generatorType
operator|=
name|this
operator|.
name|generatorType
operator|.
name|trim
argument_list|()
operator|.
name|toUpperCase
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
operator|(
name|ORACLE_TYPE
operator|.
name|equals
argument_list|(
name|this
operator|.
name|generatorType
argument_list|)
operator|||
name|NAMED_SEQUENCE_TABLE_TYPE
operator|.
name|equals
argument_list|(
name|this
operator|.
name|generatorType
argument_list|)
operator|)
condition|)
name|this
operator|.
name|generatorType
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getGeneratorType
parameter_list|()
block|{
return|return
name|generatorType
return|;
block|}
specifier|public
name|void
name|setKeyCacheSize
parameter_list|(
name|Integer
name|keyCacheSize
parameter_list|)
block|{
name|this
operator|.
name|keyCacheSize
operator|=
name|keyCacheSize
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|keyCacheSize
operator|!=
literal|null
operator|&&
name|this
operator|.
name|keyCacheSize
operator|.
name|intValue
argument_list|()
operator|<
literal|1
condition|)
block|{
name|this
operator|.
name|keyCacheSize
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|public
name|Integer
name|getKeyCacheSize
parameter_list|()
block|{
return|return
name|keyCacheSize
return|;
block|}
specifier|public
name|void
name|setGeneratorName
parameter_list|(
name|String
name|generatorName
parameter_list|)
block|{
name|this
operator|.
name|generatorName
operator|=
name|generatorName
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|generatorName
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|generatorName
operator|=
name|this
operator|.
name|generatorName
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|generatorName
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
name|this
operator|.
name|generatorName
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getGeneratorName
parameter_list|()
block|{
return|return
name|generatorName
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
literal|"{Type="
operator|+
name|generatorType
operator|+
literal|", Name="
operator|+
name|generatorName
operator|+
literal|", Cache="
operator|+
name|keyCacheSize
operator|+
literal|"}"
return|;
block|}
block|}
end_class

end_unit

