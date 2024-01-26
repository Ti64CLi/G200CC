int add(int a, int b){
    return a+b;
}

int sub(int a, int b){
    return a-b;
}

int mul(int a, int b){
    return a*b;
}

int alu(int op, int a, int b){
    if (op > 1){
        return add(a, b);
    }
    else {
        if (op > 0){
            return sub(a, b);        
        } 
        else {
            return mul(a, b);
        }
    }

     
}
