P.prototype=new Object();
P.prototype.f=1;
function P(){}
function a(){}
function m() {
                this.f++;
                var p= new P();
                a();
}
P.prototype.mm=m;
/*

class A {
}

class B extends A {
        private int f;
        private class P{}
        private void a(){}
        void m() { 
                f++;
                P p= new P();
                a();
        }       
}
*/