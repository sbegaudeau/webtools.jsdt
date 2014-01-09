function Parenizor(value) {
    this.setValue(value);
}

Parenizor.prototype.setValue=function(value) {
    this.value = value;
    return this;
};

Parenizor.prototype.getValue=function() {
    return this.value;
};

Parenizor.prototype.toString=function() {
    return '(' + this.getvalue() + ')';
};
