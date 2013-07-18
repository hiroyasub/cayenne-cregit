begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|testdo
operator|.
name|testmap
operator|.
name|auto
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
name|CayenneDataObject
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
name|exp
operator|.
name|Property
import|;
end_import

begin_comment
comment|/**  * Class _ArraysEntity was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_ArraysEntity
extends|extends
name|CayenneDataObject
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|BYTE_ARRAY_PROPERTY
init|=
literal|"byteArray"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|BYTE_WRAPPER_ARRAY_PROPERTY
init|=
literal|"byteWrapperArray"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|CHAR_ARRAY_PROPERTY
init|=
literal|"charArray"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|CHAR_WRAPPER_ARRAY_PROPERTY
init|=
literal|"charWrapperArray"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ID_PK_COLUMN
init|=
literal|"ID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|byte
index|[]
argument_list|>
name|BYTE_ARRAY
init|=
operator|new
name|Property
argument_list|<
name|byte
index|[]
argument_list|>
argument_list|(
literal|"byteArray"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Byte
index|[]
argument_list|>
name|BYTE_WRAPPER_ARRAY
init|=
operator|new
name|Property
argument_list|<
name|Byte
index|[]
argument_list|>
argument_list|(
literal|"byteWrapperArray"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|char
index|[]
argument_list|>
name|CHAR_ARRAY
init|=
operator|new
name|Property
argument_list|<
name|char
index|[]
argument_list|>
argument_list|(
literal|"charArray"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Character
index|[]
argument_list|>
name|CHAR_WRAPPER_ARRAY
init|=
operator|new
name|Property
argument_list|<
name|Character
index|[]
argument_list|>
argument_list|(
literal|"charWrapperArray"
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setByteArray
parameter_list|(
name|byte
index|[]
name|byteArray
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"byteArray"
argument_list|,
name|byteArray
argument_list|)
expr_stmt|;
block|}
specifier|public
name|byte
index|[]
name|getByteArray
parameter_list|()
block|{
return|return
operator|(
name|byte
index|[]
operator|)
name|readProperty
argument_list|(
literal|"byteArray"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setByteWrapperArray
parameter_list|(
name|Byte
index|[]
name|byteWrapperArray
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"byteWrapperArray"
argument_list|,
name|byteWrapperArray
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Byte
index|[]
name|getByteWrapperArray
parameter_list|()
block|{
return|return
operator|(
name|Byte
index|[]
operator|)
name|readProperty
argument_list|(
literal|"byteWrapperArray"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setCharArray
parameter_list|(
name|char
index|[]
name|charArray
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"charArray"
argument_list|,
name|charArray
argument_list|)
expr_stmt|;
block|}
specifier|public
name|char
index|[]
name|getCharArray
parameter_list|()
block|{
return|return
operator|(
name|char
index|[]
operator|)
name|readProperty
argument_list|(
literal|"charArray"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setCharWrapperArray
parameter_list|(
name|Character
index|[]
name|charWrapperArray
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"charWrapperArray"
argument_list|,
name|charWrapperArray
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Character
index|[]
name|getCharWrapperArray
parameter_list|()
block|{
return|return
operator|(
name|Character
index|[]
operator|)
name|readProperty
argument_list|(
literal|"charWrapperArray"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

