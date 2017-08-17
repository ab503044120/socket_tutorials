//
//  ErrorMessage.swift
//  mpush_client
//
//  Created by ohun on 16/6/17.
//  Copyright © 2016年 mpusher. All rights reserved.
//

import Foundation

final class ErrorMessage: ByteBufMessage, CustomDebugStringConvertible {
    var cmd: Int8 = 0;
    var code: Int8 = 0;
    var reason: String?;
    var data: String?;

    override func decode(body: RFIReader) {
        cmd = body.readByte();
        code = body.readByte();
        reason = body.readString();
        data = body.readString();
    }
    
    
    var debugDescription: String {
        return "ErrorMessage={cmd:\(cmd), code:\(code), reason:\(reason)}"
    }
}